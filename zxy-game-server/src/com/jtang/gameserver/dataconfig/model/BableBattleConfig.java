package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "bableBattleConfig")
public class BableBattleConfig implements ModelAdapter {

	/**
	 * 登天塔类型
	 */
	public int bableType;
	
	/**
	 * 怪物位置
	 */
	public int index;
	
	/**
	 * 怪物id
	 */
	public int monsterId;
	
	
	@Override
	public void initialize() {
	}
	

}
