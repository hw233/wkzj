package com.jtang.gameserver.dataconfig.model;

/**
 * 通用的奖励配置(物品、装备、魂魄、金币)
 * 
 * @author vinceruan
 * 
 */
public class AwardItemConfig {
	/**
	 * (1.金币 2.物品 3.装备 4.魂魄 )
	 */
	public final static int AWARD_TYPE_GOLD = 1;
	public final static int AWARD_TYPE_GOODS = 2;
	public final static int AWARD_TYPE_EQUIPS = 3;
	public final static int AWARD_TYPE_SOUL = 4;

	/**
	 * 奖励类型
	 */
	private int awardType;

	/**
	 * 对应的物品、装备、魂魄id
	 */
	private int goodsId;

	/**
	 * 奖励的数量
	 */
	private int num;

	/**
	 * 概率,默认是1000
	 */
	private int rate = 1000;

	public int getAwardType() {
		return awardType;
	}

	public void setAwardType(int awardType) {
		this.awardType = awardType;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}
}
