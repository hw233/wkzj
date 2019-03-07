package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName="powerShopConfig")
public class PowerShopConfig implements ModelAdapter{

	/**
	 * 商品id
	 */
	public int id;
	
	/**
	 * 物品id
	 */
	public int itemId;
	
	/**
	 * 类型
	 */
	public int type;
	
	/**
	 * 数量
	 */
	public int num;
	
	/**
	 * 购买消耗的物品
	 */
	public int costGoods;
	
	@Override
	public void initialize() {
		
	}

}
