package com.jtang.gameserver.module.chat.handler.response;

import com.jtang.gameserver.module.icon.model.IconVO;

public class LadderChatResponse extends ChatResponse {

	/**
	 * 消息类型
	 */
	public int type;
	
	/**
	 * 连胜场数
	 */
	public int winNum;
	
	public LadderChatResponse(int msgType, String sendName, long actorId,
			int level, int vipLevel,int type,int winNum, IconVO iconVO) {
		super(msgType, sendName, actorId, level, vipLevel, iconVO);
		this.type = type;
		this.winNum = winNum;
	}
	
	@Override
	public void write() {
		super.write();
		writeInt(type);
		writeInt(winNum);
	}

}
