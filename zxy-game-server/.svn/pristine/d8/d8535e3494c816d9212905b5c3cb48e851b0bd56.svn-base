package com.jtang.gameserver.module.crossbattle.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jiatang.common.crossbattle.CrossBattleCmd;
import com.jiatang.common.crossbattle.request.AttackActorG2W;
import com.jiatang.common.crossbattle.request.AttackActorResultG2W;
import com.jiatang.common.crossbattle.request.SignupG2W;
import com.jtang.core.protocol.ActorRequest;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.worldclient.session.WorldClientSession;

@Component
public class CrossBattleWorldPushHelper {
	@Autowired
	private WorldClientSession worldClientSession;
	private static ObjectReference<CrossBattleWorldPushHelper> ref = new ObjectReference<CrossBattleWorldPushHelper>();
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	public static void signup(SignupG2W signupRequest) {
		ActorRequest request = ActorRequest.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.W2G_SIGN_UP, signupRequest.getBytes());
		ref.get().worldClientSession.sendMsg(request);
	}

	public static void attackPlayer(long actorId, AttackActorG2W attackPlayerRequest) {
		ActorRequest request = ActorRequest.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.ATTACK_ACTOR, actorId, attackPlayerRequest.getBytes());
		ref.get().worldClientSession.sendMsg(request);
	}
	public static void attackPlayerResult(long actorId, AttackActorResultG2W attackPlayerResultRequest) {
		ActorRequest request = ActorRequest.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.G2W_POST_BATTLE_RESULT, actorId, attackPlayerResultRequest.getBytes());
		ref.get().worldClientSession.sendMsg(request);
	}
}
