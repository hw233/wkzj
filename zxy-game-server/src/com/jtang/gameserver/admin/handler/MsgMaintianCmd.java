package com.jtang.gameserver.admin.handler;

public interface MsgMaintianCmd {
	
	/**
	 * 删除msg
	 * {@code DeleteMsgRequest}
	 * {@code Response}
	 */
	byte DELETE_MSG = 1;
	
	/**
	 * 发送msg
	 * {@code SendMsgRequest}
	 * {@code Response}
	 */
	byte SEND_MSG = 2;
}
