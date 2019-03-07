package com.jtang.gameserver.module.chat.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.chat.handler.ChatCmd;
import com.jtang.gameserver.module.chat.handler.response.ChatResponse;
import com.jtang.gameserver.server.broadcast.Broadcast;

@Component
public class ChatPushHelper {
	@Autowired
	Broadcast broadcast;
	private static ObjectReference<ChatPushHelper> ref = new ObjectReference<ChatPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static ChatPushHelper getInstance() {
		return ref.get();
	}

	/**
	 * 推送消息
	 */
	public static void broabcastMsg(ChatResponse rsp) {
		Response response = Response.valueOf(ModuleName.CHAT, ChatCmd.BROADCAST_CHAT);
		response.setValue(rsp.getBytes());
		getInstance().broadcast.push2AllOnline(response);
	}
}
