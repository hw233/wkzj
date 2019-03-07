package com.jtang.gameserver.module.ally.helper;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.ally.handler.AllyCmd;
import com.jtang.gameserver.module.ally.handler.response.AllyAddResponse;
import com.jtang.gameserver.module.ally.handler.response.AllyAttributeUpdateResponse;
import com.jtang.gameserver.module.ally.handler.response.AllyFightCountResponse;
import com.jtang.gameserver.module.ally.handler.response.AllyFightResponse;
import com.jtang.gameserver.module.ally.handler.response.AllyRemoveResponse;
import com.jtang.gameserver.module.ally.model.AllyVO;
import com.jtang.gameserver.module.ally.type.AllyAttributeKey;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.server.broadcast.Broadcast;
@Component
public class AllyPushHelper {

	@Autowired
	Broadcast broadcast;

	private static ObjectReference<AllyPushHelper> ref = new ObjectReference<AllyPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static AllyPushHelper getInstance() {
		return ref.get();
	}

	public static void pushAddAlly(long actorId, AllyVO allyVO) {
		AllyAddResponse allyAddResponse = new AllyAddResponse(allyVO);
		Response response = Response.valueOf(ModuleName.ALLY, AllyCmd.PUSH_NEW_ALLY, allyAddResponse.getBytes());
		getInstance().broadcast.push(actorId, response);
	}

	public static void pushRemoveAlly(long actorId, long allyActorId) {
		AllyRemoveResponse removeResponse = new AllyRemoveResponse(allyActorId);
		Response response = Response.valueOf(ModuleName.ALLY, AllyCmd.PUSH_REMOVE_ALLY, removeResponse.getBytes());
		getInstance().broadcast.push(actorId, response);
	}

	/**
	 * 盟友相关属性的更新，比如失败次数
	 * @param actorId
	 * @param attributeMap
	 */
	public static void pushAllyAttribute(long actorId, long allyActorId, Map<AllyAttributeKey, Object> attributeMap) {
		AllyAttributeUpdateResponse recordResponse = new AllyAttributeUpdateResponse(allyActorId, attributeMap);
		Response response = Response.valueOf(ModuleName.ALLY, AllyCmd.PUSH_ALLY_ATTRIBUTE, recordResponse.getBytes());
		getInstance().broadcast.push(actorId, response);
	}
	
	/**
	 * 发送战斗结果给客户端，包括让客户端演示战斗流程以及将结果以消息的形式发送给切磋双方
	 * @param actorId
	 * @param rewardMomentum
	 * @param data
	 */
	public static void pushFightResult(long actorId, int rewardMomentum, FightData data) {
		AllyFightResponse fightResponse = new AllyFightResponse(data, rewardMomentum);
		Response response = Response.valueOf(ModuleName.ALLY, AllyCmd.ALLY_FIGHT, fightResponse.getBytes());
		getInstance().broadcast.push(actorId, response);
	}

	public static void pushAllyFightCount(long actorId, int dayFightCount) {
		AllyFightCountResponse fightCountResp = new AllyFightCountResponse(dayFightCount);
		Response response = Response.valueOf(ModuleName.ALLY, AllyCmd.PUSH_FIGHT_DAY_COUNT, fightCountResp.getBytes());
		getInstance().broadcast.push(actorId, response);
	}
}
