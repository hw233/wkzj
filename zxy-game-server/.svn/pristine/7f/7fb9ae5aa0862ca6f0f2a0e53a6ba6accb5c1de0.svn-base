package com.jtang.gameserver.module.notify.model.impl;

import java.util.List;

import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;
import com.jtang.gameserver.module.notify.type.BooleanType;

/**
 * 抢夺双方收到的通知信息
 * @author pengzy
 *
 */
public class SnatchNotifyVO extends AbstractNotifyVO {
	private static final long serialVersionUID = 1928545278443741989L;

	/**
	 * 1-抢夺；2-被抢夺
	 */
	public byte attackType;
	
	/**
	 * 抢夺物品Id
	 */
	public int goodsId;
	
	/**
	 * 抢夺物品数量
	 */
	public int goodsNum;
	
	/**
	 * 如果抢夺失败，被反抢到的物品Id
	 */
	public int snatchedGoodsId;
	
	/**
	 * 如果抢夺失败，被反抢到的物品数量
	 */
	public int snatchedGoodsNum;
	
	/**
	 * 是否有通知盟友，0未通知，1为已通知
	 */
	public BooleanType isNoticeAlly;
	
	public SnatchNotifyVO() {
		
	}
	
	public SnatchNotifyVO(byte attackType, int goodsId, int goodsNum, int snatchedGoodsId, int snatchedGoodsNum,
			BooleanType isNoticeAlly) {
		this.attackType = attackType;
		this.goodsId = goodsId;
		this.goodsNum = goodsNum;
		this.snatchedGoodsId = snatchedGoodsId;
		this.snatchedGoodsNum = snatchedGoodsNum;
		this.isNoticeAlly = isNoticeAlly;
	}

	@Override
	protected void subClazzWrite() {
		writeByte(this.attackType);
		writeInt(this.goodsId);
		writeInt(this.goodsNum);
		writeInt(this.snatchedGoodsId);
		writeInt(this.snatchedGoodsNum);
		writeByte(this.isNoticeAlly.getCode());
	}

	@Override
	protected void subClazzString2VO(String[] items) {
		attackType = Byte.valueOf(items[0]);
		goodsId = Integer.valueOf(items[1]);
		goodsNum = Integer.valueOf(items[2]);
		snatchedGoodsId = Integer.valueOf(items[3]);
		snatchedGoodsNum = Integer.valueOf(items[4]);
		isNoticeAlly = BooleanType.get(Byte.valueOf(items[5]));
	}

	@Override
	protected void subClazzParse2String(List<String> attributes) {
		attributes.add(String.valueOf(this.attackType));
		attributes.add(String.valueOf(this.goodsId));
		attributes.add(String.valueOf(this.goodsNum));
		attributes.add(String.valueOf(this.snatchedGoodsId));
		attributes.add(String.valueOf(this.snatchedGoodsNum));
		attributes.add(String.valueOf(this.isNoticeAlly));		
	}
}
