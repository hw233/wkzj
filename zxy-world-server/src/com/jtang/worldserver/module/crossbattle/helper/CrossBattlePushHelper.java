package com.jtang.worldserver.module.crossbattle.helper;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jiatang.common.crossbattle.CrossBattleCmd;
import com.jiatang.common.crossbattle.model.ActorAttributeChangeVO;
import com.jiatang.common.crossbattle.model.AttackNoticeVO;
import com.jiatang.common.crossbattle.response.ActorAttributeChangeW2G;
import com.jiatang.common.crossbattle.response.AllEndW2G;
import com.jiatang.common.crossbattle.response.EndNoticeW2G;
import com.jiatang.common.crossbattle.response.EndRewardW2G;
import com.jiatang.common.crossbattle.response.NoticeGameW2G;
import com.jiatang.common.crossbattle.response.SignupW2G;
import com.jiatang.common.crossbattle.response.TotalHurtW2G;
import com.jiatang.common.crossbattle.type.CrossBattleActorAttributeKey;
import com.jtang.core.protocol.ActorResponse;
import com.jtang.core.result.ObjectReference;
import com.jtang.worldserver.server.session.WorldSession;

@Component
public class CrossBattlePushHelper {
	private static ObjectReference<CrossBattlePushHelper> ref = new ObjectReference<CrossBattlePushHelper>();
	
	@Autowired
	private WorldSession worldSession;
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	public static void pushSignup(SignupW2G signupResponse) {
		ActorResponse response = ActorResponse.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.W2G_SIGN_UP);
		response.setValue(signupResponse.getBytes());
		ref.get().worldSession.broadcast(response);
	}
	public static void pushBattleCrossNotice(boolean start) {
		byte startByte = start? (byte)1: (byte)0;
		NoticeGameW2G noticeGameW2G = new NoticeGameW2G(startByte);
		ActorResponse response = ActorResponse.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.W2G_NOTICE_GAME);
		response.setValue(noticeGameW2G.getBytes());
		ref.get().worldSession.broadcast(response);
		
	}
	public static void pushDayEndReward(int serverId,long actorId, EndRewardW2G endRewardW2G) {
		ActorResponse response = ActorResponse.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.W2G_PUSH_DAY_END_REWARD);
		response.setActorId(actorId);
		response.setValue(endRewardW2G.getBytes());
		ref.get().worldSession.writeRespnse(serverId, response);
	}
	
	public static void pushActorAttributeChange(int serverId, long acttackActorId, Map<CrossBattleActorAttributeKey, Number> attackAttsChange, long beActtackActorId, Map<CrossBattleActorAttributeKey, Number> beAttackAttsChange ) {
		ActorAttributeChangeVO actorAttributeChangeVO = new ActorAttributeChangeVO(acttackActorId, beActtackActorId, attackAttsChange, beAttackAttsChange);
		ActorAttributeChangeW2G actorAttributeChangeW2G = new ActorAttributeChangeW2G(actorAttributeChangeVO);
		ActorResponse response = ActorResponse.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.PUSH_ACTOR_ATTRIBUTE_CHANGE);
		response.setValue(actorAttributeChangeW2G.getBytes());
		ref.get().worldSession.writeRespnse(serverId, response);
	}
	
	public static void pushTotalHurtChange(int sendTargetServerId, Map<Integer, Long> hurtMap) {
		TotalHurtW2G totalHurtW2G = new TotalHurtW2G(hurtMap);
		ActorResponse response = ActorResponse.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.PUSH_SERVER_TOTAL_HURT);
		response.setValue(totalHurtW2G.getBytes());
		ref.get().worldSession.writeRespnse(sendTargetServerId, response);
	}
	
	public static void pushBattleResult(int serverId, long actorId, short statusCode) {
		ActorResponse response = ActorResponse.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.G2W_POST_BATTLE_RESULT);
		response.setActorId(actorId);
		response.setStatusCode(statusCode);
		ref.get().worldSession.writeRespnse(serverId, response);
	}
	public static void pushNotice(int serverId, EndNoticeW2G endNoticeW2G) {
		ActorResponse response = ActorResponse.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.SYS_END_NOTICE);
		response.setValue(endNoticeW2G.getBytes());
		ref.get().worldSession.writeRespnse(serverId, response);
	}
	
	public static void pushAllEnd(int serverId, long actorId, AllEndW2G allEndW2G) {
		ActorResponse response = ActorResponse.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.ALL_END_REWARD);
		response.setActorId(actorId);
		response.setValue(allEndW2G.getBytes());
		ref.get().worldSession.writeRespnse(serverId, response);
	}
	
	public static void pushAttackNotice(int serverId, long actorId, AttackNoticeVO attackNoticeVO) {
		ActorResponse response = ActorResponse.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.PUSH_ATTACK_NOTICE);
		response.setActorId(actorId);
		response.setValue(attackNoticeVO.getBytes());
		ref.get().worldSession.writeRespnse(serverId, response);
	}
	
	
}
