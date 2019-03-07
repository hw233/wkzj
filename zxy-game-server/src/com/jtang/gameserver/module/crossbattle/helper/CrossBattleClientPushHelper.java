package com.jtang.gameserver.module.crossbattle.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jiatang.common.crossbattle.CrossBattleCmd;
import com.jiatang.common.crossbattle.response.NoticeGameW2G;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.server.broadcast.Broadcast;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class CrossBattleClientPushHelper {
	@Autowired
	private PlayerSession playerSession;
	private static ObjectReference<CrossBattleClientPushHelper> ref = new ObjectReference<CrossBattleClientPushHelper>();
	
	@Autowired
	private Broadcast broadcast;
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	public static void pushBattleResult(long actorId, FightData fightData) {
		Response response = Response.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.ATTACK_ACTOR);
		if (fightData!=null) {
			response.setValue(fightData.getBytes());
		}
		ref.get().playerSession.push(actorId, response);
		
	}

	public static void pushNoticeGame(NoticeGameW2G noticeGameW2G) {
		Response response = Response.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.W2G_NOTICE_GAME,noticeGameW2G.getBytes());
		ref.get().broadcast.push2AllOnline(response);
	}
	
	
	
}
