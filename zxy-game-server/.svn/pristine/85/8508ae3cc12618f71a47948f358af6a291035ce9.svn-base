package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;


/**
 * 店铺配置
 * 
 * */
@DataFile(fileName = "shopConfig")
public class ShopConfig implements ModelAdapter {

	/**
	 * 商品id
	 * */
	private int shopId;
	
	/**
	 * 类型 0.物品 1.装备 2.仙人魂魄 3.金币 4.仙人 5.点券
	 * */
	public int type;
	
	/**
	 * 奖励id
	 */
	public int id;
	
	/**
	 * 物品数量
	 * */
	public int number;
	
	/**
	 *购买消耗的点券 
	 * */
	private int costTicket;
	
	/**
	 * 购买消耗的金币
	 */
	public int costGold;
	
	/**
	 * 购买上限
	 * */
	private int maxBuyCount;
	
	/**
	 * 可购买vip等级 0为不限制
	 * */
	private int vipLevel;
	
	/**
	 * 可购买掌教等级(0.不限制)
	 */
	public int level;
	
	/**
	 * 重置时间 0为永久 1为每天
	 * */
	private int resetTime;
	
	/**
	 * 是否月卡才能购买(0.不是 1.是)
	 */
	public int isMonthCard;
	
	@Override
	public void initialize() {
		
	}


	public int getShopId() {
		return shopId;
	}


	public void setShopId(int shopId) {
		this.shopId = shopId;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}

	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}


	public int getCostTicket() {
		return costTicket;
	}


	public void setCostTicket(int costTicket) {
		this.costTicket = costTicket;
	}


	public int getMaxBuyCount() {
		return maxBuyCount;
	}


	public void setMaxBuyCount(int maxBuyCount) {
		this.maxBuyCount = maxBuyCount;
	}


	public int getVipLevel() {
		return vipLevel;
	}


	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}


	public int getResetTime() {
		return resetTime;
	}


	public void setResetTime(int resetTime) {
		this.resetTime = resetTime;
	}


	public boolean isMonthCard() {
		return this.isMonthCard == 1 ? true : false;
	}
	
}
