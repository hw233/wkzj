package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 吸灵室的配置
 * @author pengzy
 * 
 */
@DataFile(fileName = "vampiirConfig")
public class VampiirConfig implements ModelAdapter {

	/**
	 * 吸灵室当前等级
	 */
	private int level;
	/**
	 * 升级消耗点券
	 */
	private int upgradeTicket;

	/**
	 * 吸灵后,仙人可得到的经验附加值百分比
	 */
	private int increaseExp;

	@Override
	public void initialize() {
	}

	public int getLevel(){
		return level;
	}
	public int getUpgradeTicket(){
		return upgradeTicket;
	}
	public int getIncreaseExp(){
		return increaseExp;
	}
	
}
