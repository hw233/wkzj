package com.jtang.gameserver.module.notify.model.impl;

import java.util.List;

import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;
import com.jtang.gameserver.module.snatch.helper.SnatchHelper;

/**
 * 报仇通知盟友(A通知B，B打完后通过该消息告诉A打完了)
 * @author 0x737263
 *
 */
public class SnatchRevengeNotifyVO extends AbstractNotifyVO {
	private static final long serialVersionUID = 895575280359410699L;

	/**
	 * 抢夺类型：{@code SnatchType}
	 */
	public byte snatchType;
	
	/**
	 * 魂魄/碎片/金币的配置id
	 */
	public int goodsId;
	
	/**
	 * 获得碎片、魂魄、金币的数量
	 */
	public int goodsNum;
	
	/**
	 * 报仇角色id
	 */
	public long revengeActorId;

	public SnatchRevengeNotifyVO() {
		
	}
	
	public SnatchRevengeNotifyVO(byte snatchType, int goodsId, int goodsNum, long revengeActorId) {
		this.snatchType = snatchType;
		this.goodsId = goodsId;
		this.goodsNum = goodsNum;
		this.revengeActorId = revengeActorId;
	}
	
	@Override
	protected void subClazzWrite() {
		writeByte(this.snatchType);
		writeInt(this.goodsId);
		writeInt(this.goodsNum);
		writeLong(this.revengeActorId);
		String revengeActorName = SnatchHelper.getTargetActorName(revengeActorId);
		writeString(revengeActorName);
	}

	@Override
	protected void subClazzString2VO(String[] items) {
		snatchType = Byte.valueOf(items[0]);
		goodsId = Integer.valueOf(items[1]);
		goodsNum = Integer.valueOf(items[2]);
		revengeActorId = Long.valueOf(items[3]);
	}

	@Override
	protected void subClazzParse2String(List<String> attributes) {
		attributes.add(String.valueOf(this.snatchType));
		attributes.add(String.valueOf(this.goodsId));
		attributes.add(String.valueOf(this.goodsNum));
		attributes.add(String.valueOf(this.revengeActorId));
	}

}
