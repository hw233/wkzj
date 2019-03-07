package com.jtang.gameserver.module.chat.handler.response;

import com.jtang.gameserver.module.icon.model.IconVO;

/**
 * 聊天响应,该响应将广播给所有在线角色
 * 
 * @author pengzy
 * 
 */
public class ActorChatResponse extends ChatResponse {

	/**
	 * 聊天消息
	 */
	private String msg;

	public ActorChatResponse(int msgType, String sendName, long actorId, int level, int vipLevel, String msg,IconVO iconVO) {
		super(msgType, sendName, actorId, level, vipLevel,iconVO);
		this.msg = msg;
	}

	@Override
	public void write() {
		super.write();
		writeString(msg);
	}
}
