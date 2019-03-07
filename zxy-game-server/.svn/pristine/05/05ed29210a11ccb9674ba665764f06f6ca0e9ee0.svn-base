package com.jtang.gameserver.module.snatch.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jiatang.common.model.HeroVO;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.model.HeroUpgradeConfig;
import com.jtang.gameserver.dataconfig.model.SnatchRobotConfig;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dataconfig.service.HeroUpgradeService;
import com.jtang.gameserver.dataconfig.service.SnatchRobotService;
import com.jtang.gameserver.module.snatch.constant.SnatchRule;
import com.jtang.gameserver.module.snatch.facade.SnatchRobotFacade;
import com.jtang.gameserver.module.snatch.model.RobotMaintainVO;
import com.jtang.gameserver.module.snatch.model.SnatchEnemyVO;

@Component
public class SnatchRobotFacadeImpl implements SnatchRobotFacade,ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private Schedule schedule;
	
	/**
	 * 机器人缓存 TODO 再建一个按等级分类的map
	 * <pre>
	 * key:机器人的actorId  value:{@code SnatchEnemyVO}
	 * </pre>
	 */
	private static ConcurrentLinkedHashMap<Long, SnatchEnemyVO> ROBOT_MAPS = new ConcurrentLinkedHashMap.Builder<Long, SnatchEnemyVO>()
			.maximumWeightedCapacity(Short.MAX_VALUE).build();
	
	/**
	 * 机器人按等级缓存
	 * <pre>
	 * key:机器人角色等级  value:机器人角色id
	 * </pre>
	 */
	private static ConcurrentLinkedHashMap<Integer, List<Long>> ROBOT_LEVEL_MAPS = new ConcurrentLinkedHashMap.Builder<Integer, List<Long>>()
			.maximumWeightedCapacity(Short.MAX_VALUE).build();
	
	/**
	 * 缓存机器人的维护信息
	 */
	private static Map<Long, RobotMaintainVO> ROBOT_MAINTAIN_MAPS = new HashMap<Long, RobotMaintainVO>();
	
	/**
	 * 机器人仙人阵型缓存
	 * <pre>
	 * key:actorId  
	 * value:Map<Integer, HeroVO>,
	 * value-key:GrideIndex, value-value:HeroVO
	 * </pre>
	 */
	private static ConcurrentLinkedHashMap<Long, Map<Integer, HeroVO>> HEROS_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Map<Integer, HeroVO>>()
			.maximumWeightedCapacity(Short.MAX_VALUE).build();
	
	
	/**
	 * 定期维护机器人(5分钟)
	 */
	public Runnable maintain() {
		return new Runnable() {
			@Override
			public void run() {
				
				for (Entry<Long, RobotMaintainVO> entry : ROBOT_MAINTAIN_MAPS.entrySet()) {
					long actorId = entry.getKey();
					RobotMaintainVO mt = entry.getValue();
					mt.maintain(ROBOT_MAPS.get(actorId));
				}
			}
		};
	}
	
	/**
	 * 创建机器人
	 */
	@PostConstruct
	private void createRobot() {
		int maxLevel = SnatchRobotService.getRobotMaxLevel();
		
		for (int actorLevel = 1; actorLevel <= maxLevel; actorLevel++) {
			List<SnatchRobotConfig> levelList = SnatchRobotService.getList(actorLevel);
			for (SnatchRobotConfig robotCfg : levelList) {
				
				for (int robotIndex = 0; robotIndex < robotCfg.robotNumber; robotIndex++) {
					Map<Integer, Integer> lineUpList = robotCfg.getRandomLineUp();
					SnatchEnemyVO enemy = SnatchEnemyVO.valueOfRobot(actorLevel, robotCfg.getRobotGold());
					ROBOT_MAPS.put(enemy.actorId, enemy);
					
					//添加 机器人等级 索引缓存
					List<Long> robotActorSet = ROBOT_LEVEL_MAPS.get(enemy.actorLevel);
					if (robotActorSet == null) {
						robotActorSet = new ArrayList<>();
						ROBOT_LEVEL_MAPS.put(enemy.actorLevel, robotActorSet);
					}
					robotActorSet.add(enemy.actorId);
					
					
					// 创建机器人的仙人阵型
					Map<Integer, HeroVO> heros = new HashMap<Integer, HeroVO>();
					for (Map.Entry<Integer, Integer> entry : lineUpList.entrySet()) {
						heros.put(entry.getKey(), buildHero(entry.getValue(), robotCfg.randomLevel()));
					}
					HEROS_MAPS.put(enemy.actorId, heros);

					// 加入定时维护队列
					ROBOT_MAINTAIN_MAPS.put(enemy.actorId, new RobotMaintainVO(enemy.gold));
				}
			}
		}
	}
	
	/**
	 * 构建一个仙人
	 * @param heroId
	 * @param maxLevel
	 */
	private HeroVO buildHero(int heroId, int maxLevel) {
		HeroConfig heroConfig = HeroService.get(heroId);
		int atk = heroConfig.getAttack();
		byte atkScope = (byte) heroConfig.getAttackScope();
		int defense = heroConfig.getDefense();
		int hp = heroConfig.getHp();
		int skillId = heroConfig.getAttackSkillId();

		HeroUpgradeConfig upgradeConfig = HeroUpgradeService.get(1, heroConfig.getStar());
		int availableDelveCount = upgradeConfig.getUpgradeDelve();

		HeroVO heroVo = HeroVO.valueOf(atk, atkScope, defense, heroId, hp, skillId, availableDelveCount);

		int maxLimitLevel = HeroUpgradeService.getMaxLevel(heroConfig.getStar());
		if (maxLevel > maxLimitLevel) {
			maxLevel = maxLimitLevel;
		}
		
		// 每个等级累积攻防血
		for (int i = 1; i < maxLevel; i++) {
			HeroUpgradeConfig heroUpgradeConfig = HeroUpgradeService.get(i, heroConfig.getStar());
			heroVo.atk += heroUpgradeConfig.getUpgradeAttack(heroConfig.getUpgradeAttack());
			heroVo.defense += heroUpgradeConfig.getUpgradeDefense(heroConfig.getUpgradeDefense());
			heroVo.hp += heroUpgradeConfig.getUpgradeHp(heroConfig.getUpgradeHp());
			heroVo.level += 1;
		}
		heroVo.setMaxHp(heroVo.hp);
		return heroVo;
	}
	
	/**
	 * 获取机器人
	 * @param minLevel
	 * @param maxLevel
	 * @param num
	 * @return
	 */
	@Override
	public List<SnatchEnemyVO> randomRobotList(int minLevel, int maxLevel, int num) {
		List<SnatchEnemyVO> list = new ArrayList<>(num);

		int level = 0;
		int index = 0;
		Long actorId = 0L;
		
		List<Long> tempIds = new ArrayList<Long>();
//		for (int i = 0; i < num; i++) {
//			level = RandomUtils.nextInt(minLevel, maxLevel);
//			List<Long> robotList = ROBOT_LEVEL_MAPS.get(level);
//			index = RandomUtils.nextIntIndex(robotList.size());
//			actorId = robotList.get(index);
//			if (actorId != null && ROBOT_MAPS.containsKey(actorId)) {
//				list.add(ROBOT_MAPS.get(actorId));
//			}
//		}

		while(tempIds.size() < num) {
			level = RandomUtils.nextInt(minLevel, maxLevel);
			List<Long> robotList = ROBOT_LEVEL_MAPS.get(level);
			index = RandomUtils.nextIntIndex(robotList.size());
			actorId = robotList.get(index);
			if (actorId != null && ROBOT_MAPS.containsKey(actorId) && tempIds.contains(actorId) == false) {
				tempIds.add(actorId);
//				list.add(ROBOT_MAPS.get(actorId));
			}
		}
		for (Long acfun : tempIds) {
			list.add(ROBOT_MAPS.get(acfun));
		}
		return list;
	}
	
	@Override
	public Map<Integer, HeroVO> getRobotLineup(long actorId) {
		return HEROS_MAPS.get(actorId);
	}
	
	@Override
	public SnatchEnemyVO getSnatchEnemy(long actorId) {
		return ROBOT_MAPS.get(actorId);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		// 定时自动发放奖励
		schedule.addEverySecond(maintain(), SnatchRule.SNATCH_ROBOT_MAINTAIN_TIME);
		
	}
}
