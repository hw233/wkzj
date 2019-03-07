package com.jtang.gameserver.module.chat.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.icon.model.IconVO;

public class ChatResponse extends IoBufferSerializer {

	/**
	 * 消息类型
	 */
	public int msgType;

	/**
	 * 发起人名字
	 */
	public String sendName;

	/**
	 * 玩家id
	 */
	public long actorId;
	
	/**
	 * 玩家等级
	 */
	public int level;

	/**
	 * vip等级
	 */
	public int vipLevel;
	
	/**
	 * 头像、边框
	 */
	public IconVO iconVO;

	public ChatResponse(int msgType, String sendName,long actorId, int level, int vipLevel,IconVO iconVO) {
		this.msgType = (byte) msgType;
		this.sendName = sendName;
		this.actorId = actorId;
		this.level = level;
		this.vipLevel = vipLevel;
		this.iconVO = iconVO;
	}

	@Override
	public void write() {
		writeByte((byte)msgType);
		writeString(sendName);
		writeLong(actorId);
		writeInt(level);
		writeInt(vipLevel);
		writeBytes(iconVO.getBytes());
	}

}
