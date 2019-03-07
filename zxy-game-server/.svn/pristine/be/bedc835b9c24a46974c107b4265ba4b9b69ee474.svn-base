package com.jtang.gameserver.module.treasure.helper;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.treasure.handler.TreasureCmd;
import com.jtang.gameserver.module.treasure.handler.response.TreasureFightResponse;
import com.jtang.gameserver.module.treasure.handler.response.TreasureStateResponse;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class TreasurePushHelper {

	protected static final Logger LOGGER = LoggerFactory.getLogger(TreasurePushHelper.class);

	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<TreasurePushHelper> ref = new ObjectReference<TreasurePushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static TreasurePushHelper getInstance() {
		return ref.get();
	}

	/**
	 * 推送战斗结果
	 * 
	 * @param actorId
	 * @param treasureFightResponse
	 */
	public static void pushBattleResult(long actorId, TreasureFightResponse treasureFightResponse) {
		Response rsp = Response.valueOf(ModuleName.MAZE_TREASURE, TreasureCmd.MOVE, treasureFightResponse.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

	/**
	 * 推送迷宫寻宝状态
	 * 
	 * @param actorId
	 * @param state
	 * @param time
	 */
	public static void pushTreasureState(Long actorId, int state) {
		TreasureStateResponse response = new TreasureStateResponse(state);
		Response rsp = Response.valueOf(ModuleName.MAZE_TREASURE, TreasureCmd.PUSH_TREASURE_STATE, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);

	}
}
