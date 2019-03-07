package com.jtang.gameserver.module.chat.handler.response;

import com.jtang.gameserver.module.icon.model.IconVO;

public class SnatchChatResponse extends ChatResponse {

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
	 * 抢夺数量
	 */
	private int num;
	
	/**
	 * 赢1或输0
	 */
	private byte isWin;
	
	public SnatchChatResponse(int msgType, String actorName, long actorId, int level, int vipLevel,String targetName,
			int targetLevel, int targetVipLevel, int num,int isWin,IconVO iconVO) {
		super(msgType, actorName, actorId, level, vipLevel,iconVO);
		this.targetName = targetName;
		this.targetLevel = targetLevel;
		this.targetVipLevel = targetVipLevel;
		this.num = num;
		this.isWin = (byte) isWin;
	}

	@Override
	public void write() {
		super.write();
		writeString(targetName);
		writeInt(targetLevel);
		writeInt(targetVipLevel);
		writeInt(num);
		writeByte(isWin);
	}

}
