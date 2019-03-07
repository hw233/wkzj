package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "powerBattleConfig")
public class PowerBattleConfig implements ModelAdapter {
	
	/**
	 * 排行开始
	 */
	public int rankBegin;
	
	/**
	 * 排行结束
	 */
	public int rankEnd;
	
	/**
	 * 扣除的气势
	 */
	public int costMorale;
	
	/**
	 * 增加的气势
	 */
	public int addMorale;
	
	@Override
	public void initialize() {
		
	}

}
