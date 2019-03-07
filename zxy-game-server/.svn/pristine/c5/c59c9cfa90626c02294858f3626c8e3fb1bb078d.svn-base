package com.jtang.gameserver.module.delve.helper;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.delve.handler.DelveCmd;
import com.jtang.gameserver.module.delve.handler.response.DelveAttributeResponse;
import com.jtang.gameserver.module.delve.type.DelveAttributeKey;
import com.jtang.gameserver.server.broadcast.Broadcast;

@Component
public class DelvePushHelper {

	@Autowired
	Broadcast broadcast;
	
	private static ObjectReference<DelvePushHelper> ref = new ObjectReference<DelvePushHelper>();
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	/**
	 * 发送单个属性到客户端
	 * @param actorId
	 * @param key
	 * @param value
	 */
	public static void pushAttribute(long actorId, DelveAttributeKey key, int value) {
		DelveAttributeResponse packet = new DelveAttributeResponse();
		packet.push(key, value);
		Response response = Response.valueOf(ModuleName.DELVE, DelveCmd.PUSH_DELVE_ATTRIBUTE, packet.getBytes());
		ref.get().broadcast.push(actorId, response);
	}
	/**
	 * 发送多个属性到客户端
	 * @param actorId
	 * @param attributes
	 */
	public static void pushAttribute(long actorId, Map<DelveAttributeKey, Integer> attributes) {
		DelveAttributeResponse packet = new DelveAttributeResponse();
		packet.pushMuti(attributes);
		Response response = Response.valueOf(ModuleName.DELVE, DelveCmd.PUSH_DELVE_ATTRIBUTE, packet.getBytes());
		ref.get().broadcast.push(actorId, response);
	}
}
