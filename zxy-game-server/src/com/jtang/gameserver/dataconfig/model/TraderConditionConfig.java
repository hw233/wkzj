package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName="traderConditionConfig")
public class TraderConditionConfig implements ModelAdapter {

	/**
	 * id
	 */
	public int id;
	
	/**
	 * 类型
	 */
	public int type;
	
	/**
	 * 条件完成内容
	 */
	public int context;
	
	/**
	 * 完成条件云游仙商出现的几率(千分比几率)
	 */
	public int rate;
	
	/**
	 * 是否必出
	 * 1.必出 0.非必出
	 */
	public int must;
	
	@Override
	public void initialize() {
		
	}

}
