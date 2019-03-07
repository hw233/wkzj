package com.jtang.gameserver.module.demon.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.core.result.Result;
import com.jtang.gameserver.module.demon.handler.DemonCmd;
import com.jtang.gameserver.module.demon.handler.response.BossAttChangeResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonBossAttackResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonEndRewardResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonLastRankResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonPlayerAttackResponse;
import com.jtang.gameserver.module.demon.handler.response.FeatsChangeResponse;
import com.jtang.gameserver.module.demon.handler.response.PlayerJoinResponse;
import com.jtang.gameserver.module.demon.model.DemonVO;
import com.jtang.gameserver.server.session.PlayerSession;
@Component
public class DemonPushHelper {
	@Autowired
	private PlayerSession playerSession;
	
	private static ObjectReference<DemonPushHelper> ref = new ObjectReference<DemonPushHelper>();
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	/**
	 * 推送功勋值变化
	 * @param actorId
	 * @param featsChangeResponse
	 */
	public static void pushFeatsChange(long actorId, FeatsChangeResponse featsChangeResponse){
		Response response = Response.valueOf(ModuleName.DEMON, DemonCmd.ACOTR_FEATS_CHANGE, featsChangeResponse.getBytes());
		ref.get().playerSession.push(actorId, response);
	}
	
	/**
	 * 推送攻击玩家结果
	 * @param actorId
	 * @param demonPlayerAttackResponse
	 */
	public static void pushAttackPlayerResult(long actorId, DemonPlayerAttackResponse demonPlayerAttackResponse) {
		Response response = Response.valueOf(ModuleName.DEMON, DemonCmd.ATTACK_PLAYER, demonPlayerAttackResponse.getBytes());
		ref.get().playerSession.push(actorId, response);
	}
	
	/**
	 * 推送攻击boss结果
	 * @param actorId
	 * @param demonBossAttackResponse
	 */
	public static void pushAttackBossResult(long actorId, DemonBossAttackResponse demonBossAttackResponse) {
		Response response = Response.valueOf(ModuleName.DEMON, DemonCmd.ATTACK_BOSS, demonBossAttackResponse.getBytes());
		ref.get().playerSession.push(actorId, response);
	}
	
	/**
	 * 推送boss属性变化
	 * @param actorId
	 * @param bossAttChangeResponse
	 */
	public static void pushBossAttChange(long actorId, BossAttChangeResponse bossAttChangeResponse) {
		Response response = Response.valueOf(ModuleName.DEMON, DemonCmd.BOSS_ATT_CHANGE, bossAttChangeResponse.getBytes());
		ref.get().playerSession.push(actorId, response);
	}
	
	/**
	 * 推送加入阵营
	 * @param actorId
	 * @param demonVO
	 */
	public static void pushJoinPlayer(long actorId, DemonVO demonVO) {
		PlayerJoinResponse playerJoinResponse = new PlayerJoinResponse(demonVO);
		Response response = Response.valueOf(ModuleName.DEMON, DemonCmd.PUSH_ACTOR_JOIN, playerJoinResponse.getBytes());
		ref.get().playerSession.push(actorId, response);
	}
	
	/**
	 * 推送攻击boss失败
	 * @param actorId
	 * @param result
	 */
	public static void attackBossFail(long actorId, Result result) {
		Response response = Response.valueOf(ModuleName.DEMON, DemonCmd.ATTACK_BOSS);
		response.setStatusCode(result.statusCode);
		ref.get().playerSession.push(actorId, response);
	}
	/**
	 * 推送攻击玩家失败
	 * @param actorId
	 * @param result
	 */
	public static void attackPlayerFail(long actorId, Result result) {
		Response response = Response.valueOf(ModuleName.DEMON, DemonCmd.ATTACK_PLAYER);
		response.setStatusCode(result.statusCode);
		ref.get().playerSession.push(actorId, response);
	}

	/**
	 * 推送集众降魔奖励
	 * @param actorId
	 * @param demonEndRewardResponse
	 */
	public static void pushDemonReward(long actorId, DemonEndRewardResponse demonEndRewardResponse) {
		Response response = Response.valueOf(ModuleName.DEMON, DemonCmd.PUSH_DENMON_END_REWARD, demonEndRewardResponse.getBytes());
		ref.get().playerSession.push(actorId, response);
	}

	/**
	 * 推送排行榜
	 * @param actorId
	 * @param demonLastRankResponse
	 */
	public static void pushDemonRank(long actorId, DemonLastRankResponse demonLastRankResponse) {
		Response response = Response.valueOf(ModuleName.DEMON, DemonCmd.VIEW_LAST_RANK, demonLastRankResponse.getBytes());
		ref.get().playerSession.push(actorId, response);
	}
	
}
