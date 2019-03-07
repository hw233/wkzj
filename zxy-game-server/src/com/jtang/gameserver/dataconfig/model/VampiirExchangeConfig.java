package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "vampiirExchangeConfig")
public class VampiirExchangeConfig implements ModelAdapter {
	/**
	 * 仙人星级
	 */
	public int star;
	
	/**
	 * 兑换物品id
	 */
	public int goodsId;
	
	/**
	 * 兑换数量
	 */
	public int num;
	
	@Override
	public void initialize() {
		
	}
}
