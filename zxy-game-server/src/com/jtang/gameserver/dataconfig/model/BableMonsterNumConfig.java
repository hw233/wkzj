package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "bableMonsterNumConfig")
public class BableMonsterNumConfig implements ModelAdapter {

	/**
	 * 登天塔类型
	 */
	public int bableType;
	
	/**
	 * 层数下限
	 */
	public int floorMin;
	
	/**
	 * 层数上限
	 */
	public int floorMax;
	
	/**
	 * 前排怪物数
	 */
	public String monsterIndex1;
	
	/**
	 * 中排怪物数
	 */
	public String monsterIndex2;
	
	/**
	 * 后排怪物数
	 */
	public String monsterIndex3;
	
	@Override
	public void initialize() {
		
	}

}
