package com.jtang.gameserver.module.user.helper;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.user.handler.UserCmd;
import com.jtang.gameserver.module.user.handler.response.ActorAttributeResponse;
import com.jtang.gameserver.module.user.handler.response.ActorBuyResponse;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class ActorPushHelper {

	@Autowired
	PlayerSession playerSession;
	
	private static ObjectReference<ActorPushHelper> ref = new ObjectReference<ActorPushHelper>();
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	public static void pushAttribute(long actorId, ActorAttributeKey key, Object value) {
		ActorAttributeResponse packet = new ActorAttributeResponse(key, value);
		Response response = Response.valueOf(ModuleName.USER, UserCmd.PUSH_ACTOR_ATTRIBUTE, packet.getBytes());
		ref.get().playerSession.push(actorId, response);
	}
	
	public static void pushAttributeList(long actorId, Map<ActorAttributeKey, Object> attributeMaps) {
		ActorAttributeResponse packet = new ActorAttributeResponse(attributeMaps);
		Response response = Response.valueOf(ModuleName.USER, UserCmd.PUSH_ACTOR_ATTRIBUTE, packet.getBytes());
		ref.get().playerSession.push(actorId, response);
	}

	public static void pushActorBuyInfo(long actorId, ActorBuyResponse response) {
		Response rsp = Response.valueOf(ModuleName.USER, UserCmd.BUY_INFO,response.getBytes());
		ref.get().playerSession.push(actorId, rsp);
	}
}
