package com.jtang.gameserver.module.chat.handler.response;

import com.jtang.gameserver.module.icon.model.IconVO;

public class PowerChatResponse extends ChatResponse {

	/**
	 * 1.赢 2.输
	 */
	private int isWin;

	/**
	 * 目标名字
	 */
	private String targetName;

	/**
	 * 目标等级
	 */
	private int targetLevel;

	/**
	 * 目标vip等级
	 */
	private int targetVipLevel;
	
	/**
	 * 是否第一名
	 * 0.不是 1.是
	 */
	private byte isFirst;

	public PowerChatResponse(int msgType, String actorName, long actorId, int level, int vipLevel, int isWin, String targetName, int targetLevel,
			int targetVipLevel, int isFirst,IconVO iconVO) {
		super(msgType, actorName, actorId, level, vipLevel,iconVO);
		this.isWin = (byte) isWin;
		this.targetName = targetName;
		this.targetLevel = targetLevel;
		this.targetVipLevel = targetVipLevel;
		this.isFirst = (byte) isFirst;
	}

	@Override
	public void write() {
		super.write();
		writeByte((byte)isWin);
		writeString(targetName);
		writeInt(targetLevel);
		writeInt(targetVipLevel);
		writeByte(isFirst);
	}

}
