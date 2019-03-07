package com.jtang.gameserver.module.love.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.module.love.model.MarryAcceptVO;

public class LoveInfoResponse extends IoBufferSerializer {
	/**
	 * 伴侣角色id
	 */
	private long loveActorId;
	/**
	 * 伴侣名字
	 */
	private String loveActorName;
	
	/**
	 * 伴侣头像
	 */
	private IconVO loveActorIcon;
	
	/**
	 * 是否有礼物（0：无，1：有，2：已收）
	 */
	private byte hasGift;
	/**
	 * 是否送礼（0：否，1：是）
	 */
	private byte hasGive;
	
	private List<MarryAcceptVO> acceptList;

	public LoveInfoResponse(long loveActorId, String loveActorName,
			IconVO loveActorIcon, byte hasGift, byte hasGive, List<MarryAcceptVO> acceptList) {
		super();
		this.loveActorId = loveActorId;
		this.loveActorName = loveActorName;
		this.loveActorIcon = loveActorIcon;
		this.hasGift = hasGift;
		this.hasGive = hasGive;
		this.acceptList = acceptList;
	}

	@Override
	public void write() {
		this.writeLong(this.loveActorId);
		this.writeString(this.loveActorName);
		this.writeBytes(this.loveActorIcon.getBytes());
		this.writeByte(this.hasGift);
		this.writeByte(this.hasGive);
		this.writeShort((short) this.acceptList.size());
		for (MarryAcceptVO acceptVO : acceptList) {
			this.writeBytes(acceptVO.getBytes());
		}
	}
	
	
}
