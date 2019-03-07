package com.jtang.gameserver.module.msg.helper;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.dbproxy.entity.Message;
import com.jtang.gameserver.module.msg.handler.MsgCmd;
import com.jtang.gameserver.module.msg.handler.response.MsgRemoveResponse;
import com.jtang.gameserver.module.msg.handler.response.MsgResponse;
import com.jtang.gameserver.server.broadcast.Broadcast;
@Component
public class MsgPushHelper {

	@Autowired
	Broadcast broadcast;
	
	private static ObjectReference<MsgPushHelper> ref = new ObjectReference<MsgPushHelper>();
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	public static void pushMsg(long actorId, Message msg) {
		MsgResponse packet = new MsgResponse(msg);
		Response response = Response.valueOf(ModuleName.MSG, MsgCmd.RECEIVE_MSG, packet.getBytes());
		ref.get().broadcast.push(actorId, response);
	}
	public static void pushRemove(long toActorId, List<Long> deletedMsgList) {
		MsgRemoveResponse packet = new MsgRemoveResponse(deletedMsgList);
		Response response = Response.valueOf(ModuleName.MSG, MsgCmd.PUSH_REMOVE_MSG, packet.getBytes());
		ref.get().broadcast.push(toActorId, response);
	}

}
