package com.jtang.gameserver.module.adventures.bable.model;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 兑换物品项
 * @author ludd
 *
 */
public class ExchangeGoodsItem extends IoBufferSerializer {

	/**
	 * 物品id
	 */
	public int goodsId;
	/**
	 * 剩余数量
	 */
	private int number;
	
	/**
	 *  物品类型， 0：物品，1：装备，2：魂魄
	 */
	private byte type;
	
	/**
	 * 兑换消耗星数
	 */
	private int consumeStar;
	@Override
	public void write() {
		this.writeInt(this.goodsId);
		this.writeInt(this.number);
		this.writeByte(this.type);
		this.writeInt(this.consumeStar);
	}
	
	public ExchangeGoodsItem(int goodsId, int num, byte type, int floor, int consumeStar) {
		this.goodsId = goodsId;
		this.number = num;
		this.type = type;
		this.consumeStar = consumeStar;
	}

}
