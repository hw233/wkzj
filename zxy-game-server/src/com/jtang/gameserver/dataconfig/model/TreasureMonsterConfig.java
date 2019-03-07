package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "treasureMonsterConfig")
public class TreasureMonsterConfig implements ModelAdapter {
	
	/**
	 * 怪物id
	 */
	public int monsterId;
	
	/**
	 * 额外增加的血量
	 */
	public String hpExpr;
	
	/**
	 * 额外增加的防御
	 */
	public String defenseExpr;
	
	/**
	 * 额外增加的攻击
	 */
	public String attackExpr;

	@Override
	public void initialize() {
		
	}

}
