package com.jtang.gameserver.module.snatch.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.SnatchConfig;
import com.jtang.gameserver.dataconfig.service.GmService;
import com.jtang.gameserver.dataconfig.service.SnatchService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.icon.facade.IconFacade;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.module.snatch.constant.SnatchRule;
import com.jtang.gameserver.module.snatch.facade.SnatchActorFacade;
import com.jtang.gameserver.module.snatch.facade.SnatchEnemyFacade;
import com.jtang.gameserver.module.snatch.facade.SnatchRobotFacade;
import com.jtang.gameserver.module.snatch.model.SnatchEnemyVO;
import com.jtang.gameserver.module.snatch.type.SnatchEnemyType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;

/**
 * 抢夺的敌人接口
 * @author 0x737263
 *
 */
@Component
public class SnatchEnemyFacadeImpl implements SnatchEnemyFacade,ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SnatchEnemyFacadeImpl.class);

	@Autowired 
	ActorFacade actorFacade;
	@Autowired
	VipFacade vipFacade;
	
	@Autowired
	SnatchRobotFacade snatchRobotFacade;
	@Autowired
	SnatchActorFacade snatchActorFacade;
	@Autowired
	IconFacade iconFacade;
	
	/**
	 * 抢夺敌人列表缓存
	 * <pre>
	 * key:(actor_type), type:{@code SnatchType}  
	 * Value:List<ScoreListVO>
	 * </pre>
	 */
	private static ConcurrentLinkedHashMap<Long, List<SnatchEnemyVO>> SNATCH_ENEMY_LIST = new ConcurrentLinkedHashMap.Builder<Long, List<SnatchEnemyVO>>()
			.maximumWeightedCapacity(Short.MAX_VALUE).build();

	@Override
	public List<SnatchEnemyVO> getEnemyList(long actorId, boolean cleanCache) {
		Long key = getEnemListKey(actorId);
		if (cleanCache) {
			SNATCH_ENEMY_LIST.remove(key);
		}

		if (SNATCH_ENEMY_LIST.containsKey(key) == false || SNATCH_ENEMY_LIST.get(key).isEmpty()) {
			List<SnatchEnemyVO> list = buildEnemyList(actorId);
			SNATCH_ENEMY_LIST.put(key, list);
		}

		return SNATCH_ENEMY_LIST.get(key);
	}
	

	@Override
	public SnatchEnemyVO getEnemy(long actorId,long enemyActorId) {
		List<SnatchEnemyVO> list = getEnemyList(actorId,false);
		for(SnatchEnemyVO vo : list) {
			if(vo.actorId == enemyActorId) {
				return vo;
			}
		}
		return null;
	}

	@Override
	public void refreshEnemyList(long actorId) {
		Long key = getEnemListKey(actorId);
		SNATCH_ENEMY_LIST.remove(key);
	}
	
	private Long getEnemListKey(long actorId) {
		return actorId;
	}
	
	/**
	 * 构建当前星级下等级差的敌人列表
	 * @param actorId
	 * @param snatchType
	 * @param star
	 * @return
	 */
	private List<SnatchEnemyVO> buildEnemyList(long actorId) {
		SnatchConfig snatchCfg = SnatchService.get();
		if (snatchCfg == null) {
			return new ArrayList<>();
		}
		Actor actor = actorFacade.getActor(actorId);
		int robotMinLevel = SnatchService.robotLevelLowerLimit(actor.level);
		int robotMaxLevel = SnatchService.robotLevelUpperLimit(actor.level);
		int actorMinLevel = SnatchService.actorLevelLowerLimit(actor.level);
		int actorMaxLevel = SnatchService.actorLevelUpperLimit(actor.level);
		
		int displayNum = SnatchRule.SNATCH_ENEMY_DISPLAY_NUM;

		// 特定等级范围内抢金币只会遇到机器人
		if (SnatchRule.isRobotGoldScope(actor.level)) {
			return snatchRobotFacade.randomRobotList(robotMinLevel, robotMaxLevel, displayNum);
		}

		List<SnatchEnemyVO> enemyList = new ArrayList<>();
		enemyList.addAll(snatchRobotFacade.randomRobotList(robotMinLevel, robotMaxLevel, SnatchRule.getLeastEnemyRobotNum()));

		// 是否命中真实角色
		boolean isHit = SnatchRule.hitEnemyRealActor();
		if (isHit) {
			List<SnatchEnemyVO> realList = getRealEnemyList(actorId, actorMinLevel, actorMaxLevel, displayNum - enemyList.size());
			enemyList.addAll(realList);
			if (LOGGER.isDebugEnabled()) {
				for (SnatchEnemyVO vo : realList) {
					LOGGER.debug(String.format("real actorid:[%s] score:[%s]", vo.actorId, vo.gold));
				}
			}
		}

		// 如果不够就填机器人
		if (enemyList.size() < displayNum) {
			// 随机一个索引插进去
			int index = RandomUtils.nextIntIndex(enemyList.size());
			enemyList.addAll(index, snatchRobotFacade.randomRobotList(robotMinLevel, robotMaxLevel, displayNum - enemyList.size()));
		}

		return enemyList;
	}
	
	private List<SnatchEnemyVO> getRealEnemyList(long fiterActorId, int minLevel, int maxLevel, int num) {
		List<SnatchEnemyVO> enemyList = new ArrayList<>();

		Set<Long> ids = snatchActorFacade.randomActorIds(minLevel, maxLevel, num,fiterActorId);
		// 根据actorId组装Actor信息
		for (Long actorId : ids) {
			if (fiterActorId == actorId || GmService.isGm(actorId)) {
				continue;
			}
			Actor actor = actorFacade.getActor(actorId);
			if (actor != null) {
				int vipLevel = vipFacade.getVipLevel(actorId);
				boolean allowSnatchGold = snatchActorFacade.isAllowSnatchGold(actorId, SnatchEnemyType.ACTOR);
				if(allowSnatchGold){
					IconVO iconVO = iconFacade.getIconVO(actorId);
					enemyList.add(SnatchEnemyVO.valueOfActor(actor, vipLevel, allowSnatchGold,iconVO));
				}
			}
		}
		return enemyList;
	}


	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		ChainLock lock = LockUtils.getLock(SNATCH_ENEMY_LIST);
		try { 
			lock.lock();
			SNATCH_ENEMY_LIST.clear(); 
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
	}
	
}
