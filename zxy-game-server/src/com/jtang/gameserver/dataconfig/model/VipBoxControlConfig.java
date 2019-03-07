package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName="vipBoxControlConfig")
public class VipBoxControlConfig implements ModelAdapter {

	/**
	 * id
	 */
	public int index;
	
	/**
	 * 物品类型
	 */
	public int type;
	
	/**
	 * 限制条件
	 */
	public int context;
	
	/**
	 * 服务器单日产出数量
	 */
	public int num;
	
	@Override
	public void initialize() {
		
	}

}
