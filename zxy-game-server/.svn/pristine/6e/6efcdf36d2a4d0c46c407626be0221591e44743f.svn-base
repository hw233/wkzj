package com.jtang.gameserver.module.user.facade.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jtang.core.event.EventBus;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.component.event.ContinueLoginEvent;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Online;
import com.jtang.gameserver.module.user.dao.OnlineDao;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.OnlineFacade;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 
 * @author 0x737263
 *
 */
@Component
public class OnlineFacadeImpl implements OnlineFacade, ApplicationListener<ContextRefreshedEvent>,ZeroListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(OnlineFacadeImpl.class);
	@Autowired
	OnlineDao onlineDao;
	@Autowired
	ActorFacade actorFacade;
	@Autowired
	PlayerSession playerSession;
	@Autowired
	EventBus eventBus;
	@Autowired
	Schedule schedule;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 服务器启动,清除online表
		int removeCount = onlineDao.cleanOnline(Game.getServerId());
		LOGGER.info(String.format("remove [%s] online record finish.", removeCount));
	}

	@Override
	public void add(long actorId, String sim, String mac, String imei, String ip) {
		Actor actor = actorFacade.getActor(actorId);
		Online online = Online.valueOf(actorId, actor.platformType, actor.serverId, actor.uid, actor.channelId, sim, mac, imei, ip);
		onlineDao.add(online);
	}

	@Override
	public Online remove(long actorId) {
		Online online = onlineDao.get(actorId);
		try {
			if (online == null) {
				Actor actor = actorFacade.getActor(actorId);
				online = Online.valueOf(actorId, actor.platformType, actor.serverId, actor.uid, actor.channelId, "", "", "", "");
			}
		} catch (Exception ex) {
			LOGGER.error("", ex);
		} finally {
			onlineDao.remove(actorId);
		}
		return online;
	}

	@Override
	public void onZero() {
		// 启动一个调度，每天0点获取在线用户，给这些玩家抛出连续登陆事件
		Set<Long> actorIds = playerSession.onlineActorList();
		for (Long actorId : actorIds) {
			if (playerSession.isOnline(actorId)) {
				Actor actor = actorFacade.getActor(actorId);
				// 连续登陆天数有变化，则抛事件
				if (actor != null && actor.calculateContinueDays()) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(String.format("continue login event send actorId:%s", actorId));
					}

					eventBus.post(new ContinueLoginEvent(actorId, actor.continueDays));
				}
			}
		}
	}
	
}
