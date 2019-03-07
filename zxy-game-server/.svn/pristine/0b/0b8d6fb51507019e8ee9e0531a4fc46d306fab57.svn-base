package com.jtang.gameserver.module.snatch.facade.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.AbstractReceiver;
import com.jtang.core.event.Event;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.ActorLevelUpEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.service.SnatchService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Snatch;
import com.jtang.gameserver.module.snatch.constant.SnatchRule;
import com.jtang.gameserver.module.snatch.facade.SnatchActorFacade;
import com.jtang.gameserver.module.snatch.facade.SnatchEnemyFacade;
import com.jtang.gameserver.module.snatch.facade.SnatchFacade;
import com.jtang.gameserver.module.snatch.model.SnatchActorMap;
import com.jtang.gameserver.module.snatch.type.SnatchEnemyType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.ActorFetchFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;

/**
 * 
 * @author 0x737263
 *
 */
@Component
public class SnatchActorFacadeImpl extends AbstractReceiver implements SnatchActorFacade, ActorLoginListener ,ZeroListener {

	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private ActorFetchFacade actorFetchFacade;
	@Autowired
	private SnatchFacade snatchFacade;
	@Autowired
	private Schedule schedule;
	@Autowired
	private SnatchEnemyFacade snatchEnemyFacade;
	
	/**
	 * 抢夺 角色\等级 缓存索引类 
	 */
	private static SnatchActorMap SNATCH_ACTOR = new SnatchActorMap();
	
	/**
	 * 角色累计"被"抢夺的金币总数(每天凌晨清零)
	 * <pre> key:角色id value:被抢夺金币累计总数 </pre>
	 */
	private static ConcurrentHashMap<Long, Integer> SNATCH_ACTOR_GOLD_MAPS = new ConcurrentHashMap<>();
	
	/**
	 * 系统启动时将最近登陆的活跃玩家加入抢夺缓存列表
	 */
	@PostConstruct
	private void init() {
		
		int num = SnatchRule.SNATCH_ACTOR_NUM_4_CACHE;
		int loginTime = TimeUtils.getNow() - SnatchRule.SNATCH_LOGIN_DAY_4_CACHE;
		Map<Long, Integer> map = actorFetchFacade.getLevelList(loginTime, num);
		if (map != null) {
			for (Entry<Long, Integer> entry : map.entrySet()) {
				long actorId = entry.getKey();
				int level = entry.getValue();
				SNATCH_ACTOR.addActorLevel(actorId, 0, level);
			}
		}
		
		//添加积分
		for (Long actorId : map.keySet()) {
			Snatch snatch = snatchFacade.get(actorId);
			if (snatch != null) {
				SNATCH_ACTOR.addActorScore(actorId, snatch.score, 0);
			}
		}
		
	}

	@Override
	public Set<Long> randomActorIds(int minLevel, int maxLevel, int num,long actorId) {
		Set<Long> ids = new HashSet<>();

		List<Long> list = new ArrayList<Long>();
		for (int index = minLevel; index <= maxLevel; index++) {
			Set<Long> set = SNATCH_ACTOR.getLevelActorIds(index);
			if (set != null) {
				list.addAll(set);
				if(list.contains(actorId)){
					list.remove(actorId);
				}
			}
		}

		if (list.size() > 0) {
			int nums[] = RandomUtils.uniqueRandom(num, 0, list.size() - 1);
			for (int ind : nums) {
				ids.add(list.get(ind));
			}
		}
		return ids;
	}

	@Override
	public String[] getEventNames() {
		return new String[] { EventKey.ACTOR_LEVEL_UP};
	}
	
	@Override
	public void onEvent(Event paramEvent) {

		if (paramEvent.name == EventKey.ACTOR_LEVEL_UP) {
			ActorLevelUpEvent event = paramEvent.convert();
			// 将旧等级对应的actorId移除
			long actorId = event.actor.getPkId();
			int oldLevel = event.oldLevel;
			int newLevel = event.actor.level;
			SNATCH_ACTOR.addActorLevel(actorId, oldLevel, newLevel);
		} 
		
//		if (paramEvent.name == EventKey.SNATCH_RESULT) {
//			SnatchResultEvent snatchResult = paramEvent.convert();
//			
//			//抢夺金币特殊处理
//			if (snatchResult.snatchType == SnatchType.GOLD) {
//				if (snatchResult.snatchEnemyType != SnatchEnemyType.ACTOR) {
//					return;
//				}
//
//				long failActorId = 0L;
//				// 找到输的一方，给他加上被抢金币总数
//				if (snatchResult.winLevel.isWin()) {
//					failActorId = snatchResult.targetActorId;
//				} else {
//					failActorId = snatchResult.actorId;
//				}
//
//				Integer gold = SNATCH_ACTOR_GOLD_MAPS.get(failActorId);
//				if (gold == null) {
//					gold = 0;
//				}
//				gold += snatchResult.goodsNum;				
//				SNATCH_ACTOR_GOLD_MAPS.put(failActorId, gold);
//			}
//		}
		
		
	}

	@Override
	public void onLogin(long actorId) {
		// 角色登陆添加到角色索引中
		Actor actor = this.actorFacade.getActor(actorId);
		SNATCH_ACTOR.addActorLevel(actorId, 0, actor.level);

		Snatch s = this.snatchFacade.get(actorId);
		SNATCH_ACTOR.addActorScore(actorId, s.score, 0);
	}
	
	@Override
	public boolean isAllowSnatchGold(long actorId, SnatchEnemyType enemyType) {
		if(enemyType == SnatchEnemyType.ROBOT) {
			return true;
		}
		
		Actor actor = this.actorFacade.getActor(actorId);
		if (actor == null) {
			return true;
		}

		// 累计已被抢的金币
		Integer gold = SNATCH_ACTOR_GOLD_MAPS.get(actorId);
		if (gold == null) {
			gold = 0;
			SNATCH_ACTOR_GOLD_MAPS.put(actorId, gold);
			return true;
		}

		// 当前角色的上限值
		int limitGold = SnatchService.snatchGoldLimit(actor.level);
		if (gold < limitGold) {
			return true;
		}

		return false;
	}

	@Override
	public int allowSnatchGoldNum(long actorId, int appendGold) {
		Actor actor = this.actorFacade.getActor(actorId);
		if (actor == null) {
			return appendGold;
		}

		// 当前角色的上限值
		int limitGold = SnatchService.snatchGoldLimit(actor.level);

		//累计已被抢的金币
		Integer gold = SNATCH_ACTOR_GOLD_MAPS.get(actorId);
		if (gold == null) {
			gold = 0;
			SNATCH_ACTOR_GOLD_MAPS.put(actorId, gold);
		}

		// 已达上限
		if (gold >= limitGold) {
			return 0;
		}

		// 被抢的金币大于上限,返回还能抢的金币数
		if (gold + appendGold > limitGold) {
			SNATCH_ACTOR_GOLD_MAPS.put(actorId, limitGold);
			return limitGold - gold;
		}
		
		if(appendGold > 0) {
			SNATCH_ACTOR_GOLD_MAPS.put(actorId, gold + appendGold);	
		}
		
		return appendGold;
	}

	@Override
	public void onZero() {
		ChainLock lock = LockUtils.getLock(SNATCH_ACTOR_GOLD_MAPS);
		try {
			lock.lock();
			SNATCH_ACTOR_GOLD_MAPS.clear();
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
	}

}
