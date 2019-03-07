package com.jtang.gameserver.component.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.cache.AbstractCacheListener;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.concurrent.ConcurrentLinkedQueueHashMap;
import com.jtang.core.db.DBQueue;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.listener.ActorLogoutListener;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 全局缓存管理
 * >接收退出的角色id加入队列
 * >定时过滤这些队列.如果当前用户在线则从队列中移除
 * >如果该actorId不在线，并且在db队列中不存在.则开始清除各模块Dao中的LRU缓存
 * @author 0x737263
 *
 */
@Component
public class CacheListenerImpl extends AbstractCacheListener implements ActorLogoutListener {
	@Autowired
	Schedule schedule;
	@Autowired
	PlayerSession playerSession;
	@Autowired
	DBQueue dbQueue;
	@Autowired
	ActorFacade actorFacade;
	
	/**
	 * 清除缓存的角色队列
	 */
	private static ConcurrentLinkedQueueHashMap<Long, Integer> LOGOUT_ACTOR_ID_QUEUE = new ConcurrentLinkedQueueHashMap<>(Byte.MAX_VALUE);
	
	@Override
	protected void init() {
		
		schedule.addEveryMinute(new Runnable() {
			@Override
			public void run() {
				if (LOGOUT_ACTOR_ID_QUEUE.isEmpty()) {
					return;
				}

				List<Entry<Long, Integer>> addList = new ArrayList<>();
				int remaingNum = 0; // 队列剩余数量
				for (int i = 0; i < eachCleanActorNum; i++) {
					Entry<Long, Integer> entry = LOGOUT_ACTOR_ID_QUEUE.poll();
					if (entry == null) {
						break;
					}

					Long actorId = entry.getKey();
					int logoutTime = entry.getValue();

					Actor actor = actorFacade.getActor(actorId);
					if (actor == null) {
						continue;
					}

					// 退出时间内又登陆上来了
					if (actor.loginTime > logoutTime) {
						continue;
					}

					// 退出超过xx分钟，则开始清理
					if (TimeUtils.getNow() >= logoutTime + logoutExpiredTime) {
						// 执行清除缓存操作
						for (Entry<String, CacheListener> listener : CACHE_LISTENER_LIST) {
							if (playerSession.isOnline(actorId) == false && dbQueue.actorInQueue(actorId) == false) {
								remaingNum = listener.getValue().cleanCache(actorId);
								CACHE_LOGGER.info(String.format("module:[%s] clean actorId:[%s] success. remaining:[%s]", listener.getKey(), actorId,
										remaingNum));
							}
						}
						CACHE_LOGGER.info(String.format("clean actorId:[%s]", actorId));
					} else {
						addList.add(entry);
					}
					
					CACHE_LOGGER.info("clean complete!------------");
				}

				// 没达到清除规则的，则添加回队列
				for (Entry<Long, Integer> entry : addList) {
					LOGOUT_ACTOR_ID_QUEUE.set(entry.getKey(), entry.getValue());
				}
			}
		}, triggerScheduleTime);
		LOGGER.info(String.format("cache listener facade init complete! eachCleanActorNum:%s/num logoutExpiredTime:%s/sec scheduleTime:%s/min",
				eachCleanActorNum, logoutExpiredTime, triggerScheduleTime));
	}

	/**
	 * 清理指定actor的数据缓存
	 * @param actorId
	 */
	@Override
	public void clearSpecifyActorId(long actorId) {
		int remaingNum = 0;
		for (Entry<String, CacheListener> listener : CACHE_LISTENER_LIST) {
			remaingNum = listener.getValue().cleanCache(actorId);
			CACHE_LOGGER.info(String.format("module:[%s] clean specify actorId:[%s] success. remaining:[%s]", listener.getKey(), actorId, remaingNum));
		}
		CACHE_LOGGER.info(String.format("clean actorId:[%s] complete---------", actorId));
	}
	@Override
	public void onLogout(long actorId) {
		// 更新为最新的退出时间
		LOGOUT_ACTOR_ID_QUEUE.set(actorId, TimeUtils.getNow());
	}
}
