package com.jtang.gameserver.module.ladder.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.dataconfig.service.LadderService;
import com.jtang.gameserver.dbproxy.entity.Ladder;
import com.jtang.gameserver.module.ladder.handler.LadderCmd;
import com.jtang.gameserver.module.ladder.handler.response.FightNumResponse;
import com.jtang.gameserver.module.ladder.handler.response.LadderFightInfoResponse;
import com.jtang.gameserver.module.ladder.handler.response.LadderFightResponse;
import com.jtang.gameserver.module.ladder.handler.response.LadderPushResponse;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class LadderPushHelper {

	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<LadderPushHelper> REF = new ObjectReference<>();

	@PostConstruct
	protected void init() {
		REF.set(this);
	}

	private static LadderPushHelper getInstance() { 
		return REF.get();
	}
	
	public static void pushBattleResult(long actorId,LadderFightResponse response){
		Response rsp = Response.valueOf(ModuleName.LADDER, LadderCmd.START_FIGHT,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

	public static void pushFightNum(Long actorId, Ladder ladder) {
		FightNumResponse response = new FightNumResponse();
		response.fightNum = ladder.fightNum;
		int nextFlushTime = ladder.flushFightTime + LadderService.getGlobalConfig().flushTime;
		response.flushTime = nextFlushTime;
		Response rsp = Response.valueOf(ModuleName.LADDER, LadderCmd.PUSH_FIGHT_NUM,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushSportEnd(Long actorId) {
		Response rsp = Response.valueOf(ModuleName.LADDER, LadderCmd.PUSH_SPORT_END);
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void push(Long actorId,LadderPushResponse response) {
		Response rsp = Response.valueOf(ModuleName.LADDER, LadderCmd.PUSH_LADDER,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushLadderFightInfo(long actorId,LadderFightInfoResponse response){
		Response rsp = Response.valueOf(ModuleName.LADDER, LadderCmd.PUSH_FIGHT_INFO,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
}
