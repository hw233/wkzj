package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName="vipShopConfig")
public class VipShopConfig implements ModelAdapter {
	
	/**
	 * 商品id
	 */
	public int id;
	
	/**
	 * 需要vip等级
	 */
	public int vipLevel;
	
	/**
	 * 需要的点券
	 */
	public int costTicket;
	
	/**
	 * 物品id
	 */
	public int rewardId;
	
	/**
	 * 物品类型
	 */
	public int rewardType;
	
	/**
	 * 物品数量
	 */
	public int rewardNum;
	
	/**
	 * 每天购买上限次数
	 */
	public int dayMaxNum;
	
	/**
	 * 物品总购买上限
	 */
	public int maxNum;
	
	@Override
	public void initialize() {
		
	}

}
