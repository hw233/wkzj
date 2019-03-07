package com.jtang.gameserver.module.msg.handler;

public interface MsgCmd {

	/**
	 * 推送：客户端获取角色消息列表
	 * <pre>
	 * 请求：{@code Request}
	 * 回复：{@code MsgListResponse}
	 * </pre>
	 */
	byte GET_MSG_LIST = 1;
	/**
	 * 客户端请求发送消息给某个角色，验证成功后，服务器会将消息以MsgResponse的形式转发给接收者
	 * 请求：{@code SendMsgRequest}
	 * 回复：{@code MsgResponse}
	 */
	byte SEND_MSG = 2;
	
	/**
	 * 推送：服务端将客户端发送的消息转发给此消息的接收者
	 * 回复：{@code MsgResponse}
	 */
	byte RECEIVE_MSG = 3;
	
	/**
	 * 推送：当角色的消息条数超过了限制，则通知客户端删除
	 * 回复：{@code MsgRemoveResponse}
	 */
	byte PUSH_REMOVE_MSG = 4;
	/**
	 * 删除留言请求
	 * 请求：{@code RemoveMsgRequest}
	 * 回复：{@code Response}
	 */
	byte REMOVE_MSG = 5;
	
	/**
	 * 将留言设为已读
	 * 请求：{@code Request}
	 * 回复：{@code Response}
	 */
	byte SET_READED = 6;
}
