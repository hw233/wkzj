package com.jtang.gameserver.module.notify.model.impl;

import java.util.List;

import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;
import com.jtang.gameserver.module.notify.type.BooleanType;
import com.jtang.gameserver.module.snatch.helper.SnatchHelper;

/**
 * 抢夺或被抢夺通知盟友的信息结构
 * @author pengzy
 *
 */
public class SnatchAllyNotifyVO extends AbstractNotifyVO {
	private static final long serialVersionUID = -5393693928664132450L;

	/**
	 * 1-主攻；2-被攻
	 */
	public byte attackType;
	
	/**
	 * 抢夺目标
	 */
	public long targetActorId;

	/**
	 * 抢夺物品Id
	 */
	public int goodsId;
	
	/**
	 * 抢夺物品数量
	 */
	public int goodsNum;
	
	/**
	 * 是否有通知盟友，0未通知，1为已通知
	 */
	public byte isNoticeAlly;

	public SnatchAllyNotifyVO() {
		
	}
	
	public SnatchAllyNotifyVO(byte attackType, long targetActorId, int goodsId, int goodsNum) {
		this.attackType = attackType;
		this.targetActorId = targetActorId;
		this.goodsId = goodsId;
		this.goodsNum = goodsNum;
		this.isNoticeAlly = BooleanType.FALSE.getCode();
	}

	@Override
	protected void subClazzWrite() {
		writeByte(attackType);
		writeLong(targetActorId);
		writeInt(goodsId);
		writeInt(goodsNum);
		String targetActorName = SnatchHelper.getTargetActorName(this.targetActorId);
		writeString(targetActorName);
	}

	@Override
	protected void subClazzString2VO(String[] items) {
		attackType = Byte.valueOf(items[0]);
		targetActorId = Long.valueOf(items[1]);
		goodsId = Integer.valueOf(items[2]);
		goodsNum = Integer.valueOf(items[3]);
	}

	@Override
	protected void subClazzParse2String(List<String> attributes) {
		attributes.add(String.valueOf(this.attackType));
		attributes.add(String.valueOf(this.targetActorId));
		attributes.add(String.valueOf(this.goodsId));
		attributes.add(String.valueOf(this.goodsNum));
	}
	
}
