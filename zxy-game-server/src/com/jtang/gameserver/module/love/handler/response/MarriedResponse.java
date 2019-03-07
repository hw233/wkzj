package com.jtang.gameserver.module.love.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.icon.model.IconVO;

public class MarriedResponse extends IoBufferSerializer {
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
	
	
	
	public MarriedResponse(long loveActorId, String loveActorName,
			IconVO loveActorIcon) {
		super();
		this.loveActorId = loveActorId;
		this.loveActorName = loveActorName;
		this.loveActorIcon = loveActorIcon;
	}



	@Override
	public void write() {
		this.writeLong(this.loveActorId);
		this.writeString(this.loveActorName);
		this.writeBytes(this.loveActorIcon.getBytes());
	}
}
