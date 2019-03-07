package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "giveEquipReward")
public class GiveEquipRewardConfig implements ModelAdapter {
	
	/**
	 * 装备id
	 */
	public int equipId;
	
	/**
	 * 数量
	 */
	public int num;
	
	/**
	 * 几率
	 */
	public int proportion;

	@Override
	public void initialize() {
		
	}

}
