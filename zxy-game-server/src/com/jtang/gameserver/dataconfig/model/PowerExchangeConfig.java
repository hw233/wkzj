package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "powerExchangeConfig")
public class PowerExchangeConfig implements ModelAdapter {

	/**
	 * 兑换id
	 */
	public int exchangeId;
	
	/**
	 * 奖励类型
	 */
	public int exType;
	
	/**
	 * 奖励id
	 */
	public int id;
	
	/**
	 * 购买一次数量
	 */
	public int num;
	
	/**
	 * 购买一次扣势力点
	 */
	public int costPowerNum;
	
	@Override
	public void initialize() {
		
	}

}
