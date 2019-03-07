package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 抢夺对手等级配置
 * @author liujian
 *
 */
@DataFile(fileName="snatchEnemyLevelConfig")
public class SnatchEnemyLevelConfig implements ModelAdapter {

	/**
	 * 掌教等级
	 */
	private int actorLevel;

	/**
	 * 抢夺机器人对手，机器人的等级下限
	 */
	private int robotLowerLimit;
	
	/**
	 * 抢夺机器人对手,机器人的等级上限
	 */
	private int robotUpperLimit;
	
	/**
	 * 抢夺真实玩家对手，角色的等级下限
	 */
	private int actorLowerLimit;
	
	/**
	 * 抢夺真实玩家对手，角色的等级上限
	 */
	private int actorUpperLimit;
	
	/**
	 * 允许“被”抢夺的金币上限
	 */
	private int snatchGoldLimit;
		
	@Override
	public void initialize() {
	}

	public int getActorLevel() {
		return actorLevel;
	}

	public int getRobotLowerLimit() {
		return robotLowerLimit;
	}

	public int getRobotUpperLimit() {
		return robotUpperLimit;
	}

	public int getActorLowerLimit() {
		return actorLowerLimit;
	}

	public int getActorUpperLimit() {
		return actorUpperLimit;
	}

	public int getSnatchGoldLimit() {
		return snatchGoldLimit;
	}
	
}
