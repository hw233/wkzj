package com.jtang.gameserver.module.adventures.achievement.helper;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.adventures.achievement.handler.AchieveCmd;
import com.jtang.gameserver.module.adventures.achievement.handler.response.AchieveAttributeResponse;
import com.jtang.gameserver.module.adventures.achievement.handler.response.AchieveResponse;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveAttributeKey;
import com.jtang.gameserver.server.broadcast.Broadcast;
/***
 * 
 * @author pengzy
 *
 */
@Component
public class AchievePushHelper {
	@Autowired
	Broadcast broadcast;

	private static ObjectReference<AchievePushHelper> ref = new ObjectReference<AchievePushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static AchievePushHelper getInstance() {
		return ref.get();
	}

	public static void pushAchievement(long actorId, List<AchieveVO> list) {
		AchieveResponse packet = new AchieveResponse(list);
		Response response = Response.valueOf(ModuleName.ACHIEVEMENT, AchieveCmd.PUSH_ACHIEVEMENT, packet.getBytes());
		getInstance().broadcast.push(actorId, response);
	}
	

	public static void pushAttribute(long actorId, int achieveId, Map<AchieveAttributeKey, Object> changedValues) {
		AchieveAttributeResponse packet = new AchieveAttributeResponse(achieveId, changedValues);
		Response response = Response.valueOf(ModuleName.ACHIEVEMENT, AchieveCmd.PUSH_ACHIEVE_ATTRIBUTE, packet.getBytes());
		ref.get().broadcast.push(actorId, response);
	}
}
