package com.jtang.gameserver.module.hole.help;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.hole.handler.HoleCmd;
import com.jtang.gameserver.module.hole.handler.response.AllyFightNumResponse;
import com.jtang.gameserver.module.hole.handler.response.AllyHoleNotifyResponse;
import com.jtang.gameserver.module.hole.handler.response.HoleFightResponse;
import com.jtang.gameserver.module.hole.handler.response.HoleOpenResponse;
import com.jtang.gameserver.module.hole.model.HoleNotifyVO;
import com.jtang.gameserver.module.hole.model.HoleVO;
import com.jtang.gameserver.server.broadcast.Broadcast;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class HolePushHelp {

	protected static final Logger LOGGER = LoggerFactory.getLogger(HolePushHelp.class);

	@Autowired
	PlayerSession playerSession;

	@Autowired
	Broadcast broadcast;

	private static ObjectReference<HolePushHelp> ref = new ObjectReference<HolePushHelp>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static HolePushHelp getInstance() {
		return ref.get();
	}

	/**
	 * 战斗处理失败也需要下发
	 * 
	 * @param actorId
	 * @param statusCode
	 */
	public static void pushBattleFailResult(long actorId, short statusCode) {
		Response rsp = Response.valueOf(ModuleName.HOLE, HoleCmd.HOLE_FIGHT);
		rsp.setStatusCode(statusCode);
		getInstance().playerSession.push(actorId, rsp);
	}

	/**
	 * 推送战斗结果
	 * 
	 * @param actorId
	 * @param battleResult
	 */
	public static void pushBattleResult(long actorId, HoleFightResponse holeFightResponse) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("\r\n" + holeFightResponse.format("") + "\r\n\r\n");
		}
		Response rsp = Response.valueOf(ModuleName.HOLE, HoleCmd.HOLE_FIGHT, holeFightResponse.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

//	/**
//	 * 上古洞府关闭
//	 * 
//	 * @param actorId
//	 */
//	public static void pushHoleClose(long actorId, long id) {
//		HoleCloseResponse response = new HoleCloseResponse(id);
//		Response rsp = Response.valueOf(ModuleName.HOLE, HoleCmd.PUSH_HOLE_DISAPPER, response.getBytes());
//		getInstance().playerSession.push(actorId, rsp);
//	}

	/**
	 * 开启上古洞府
	 */
	public static void pushHoleOpen(long actorId, HoleVO holeVO) {
		HoleOpenResponse response = new HoleOpenResponse(holeVO);
		Response rsp = Response.valueOf(ModuleName.HOLE, HoleCmd.PUSH_HOLE_OPEN, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	/**
	 * 盟友通关数量
	 * @param actorId
	 * @param allyNum 通关盟友数量
	 */
	public static void pushAllyNum(long actorId, int allyNum) {
		AllyFightNumResponse response = new AllyFightNumResponse(allyNum);
		Response rsp = Response.valueOf(ModuleName.HOLE, HoleCmd.PUSH_ALLY_NUM, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushAllyHoleVO(long actorId, HoleNotifyVO holeNotifyVO) {
		AllyHoleNotifyResponse allyHoleVOResponse = new AllyHoleNotifyResponse(holeNotifyVO);
		Response rsp = Response.valueOf(ModuleName.HOLE, HoleCmd.PUSH_ALLY_HOLE, allyHoleVOResponse.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushZero(long actorId) {
		Response rsp = Response.valueOf(ModuleName.HOLE, HoleCmd.PUSH_ZERO);
		getInstance().playerSession.push(actorId, rsp);
	}
}
