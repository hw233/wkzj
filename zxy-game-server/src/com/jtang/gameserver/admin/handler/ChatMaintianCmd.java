package com.jtang.gameserver.admin.handler;

public interface ChatMaintianCmd {

	/**
	 * 禁言
	 * 请求:{@code ActorChatForbiddenRequest}
	 * 返回:{@code Response}
	 */
	byte FORBIDDEN_CHAT = 1;
	
	/**
	 * 解除禁言
	 * 请求:{@code ActorChatUnforbiddenRequest}
	 * 返回:{@code Response}
	 */
	byte UNFORBIDDEN_CHAT = 2;
}
