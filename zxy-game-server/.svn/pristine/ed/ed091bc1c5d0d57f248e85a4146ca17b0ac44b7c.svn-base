package com.jtang.gameserver.module.snatch.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.module.snatch.constant.SnatchRule;

/**
 * 机器人的一些扩展信息
 * 这些信息不需要下发给客户端, 只用于后台线程根据这些信息定期维护机器人
 * @author vinceruan
 *
 */
public class RobotMaintainVO {
	private static final Logger LOGGER = LoggerFactory.getLogger(RobotMaintainVO.class);
	
	/**
	 * 金币增长的上限
	 * 达到该上限金币停止增长
	 */
	public int goldUpperLimit;
	
	/**
	 * 金币减少的下限
	 * 达到该下限后金币开始回复
	 */
	public int goldLowerLimit;
	
	/**
	 * 是否正在回复
	 */
	public boolean addingGold = false;
		
	/**
	 * 机器人基本信息
	 */
	public long actorId;
	
	/**
	 * 开始维护
	 */
	public void maintain(SnatchEnemyVO robot) {
		//少于下限就开启恢复标记		
		if (robot.gold < goldLowerLimit && addingGold == false) {
			addingGold = true;
		}
		
		//高于上限就停止回复标记
		if (robot.gold >= goldLowerLimit) {
			addingGold = false;
		}
		
		//加金币到机器人身上
		if (addingGold) {
			int randomGold = RandomUtils.nextInt(goldLowerLimit, goldUpperLimit);
			int addGold = (randomGold * SnatchRule.SNATCH_ROBOT_GOLD_EACH_RECOER) / 100;
			robot.gold += addGold;
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("robot actorid:[%s] level:[%s] current gold:[%s] add gold:[%s]", robot.actorId, robot.actorLevel,
						robot.gold, addGold));
			}
		}
	}
	
	public RobotMaintainVO(long maxGold) {
		this.addingGold = false;
		this.goldUpperLimit = (int) maxGold;
		this.goldLowerLimit = (goldUpperLimit * SnatchRule.SNATCH_ROBOT_GOLD_LOWER_LIMIT) / 100;
	}
	
}
