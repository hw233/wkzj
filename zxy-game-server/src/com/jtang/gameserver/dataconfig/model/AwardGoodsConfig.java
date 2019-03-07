package com.jtang.gameserver.dataconfig.model;

/**
 * 单个物品掉落配置
 * @author vinceruan
 *
 */
public class AwardGoodsConfig {
	private int goodsId;
	private int num;
	private int rate;
	
	public AwardGoodsConfig(int goodsId, int num, int rate) {
		this.goodsId = goodsId;
		this.num = num;
		this.rate = rate;
	}
	
	public int getGoodsId() {
		return goodsId;
	}
	
	public int getNum() {
		return num;
	}
	
	public int getRate() {
		return rate;
	}
	
}
