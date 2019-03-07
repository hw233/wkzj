package com.jtang.gameserver.module.power.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.power.handler.PowerCmd;
import com.jtang.gameserver.module.power.handler.response.PowerFightResponse;
import com.jtang.gameserver.server.broadcast.Broadcast;
//import com.jtang.sm2.module.power.handler.response.AllyChangeResponse;

@Component
public class PowerPushHelper {
	@Autowired
	Broadcast broadcast;


	private static ObjectReference<PowerPushHelper> ref = new ObjectReference<PowerPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static PowerPushHelper getInstance() {
		return ref.get();
	}

	/**
	 * 推送战斗结果
	 * @param actorId
	 * @param data
	 * @param targetRank
	 * @param targetActorName
	 * @param captureSuccess
	 * @param herosouls
	 */
	public static void pushFightResult(long actorId, PowerFightResponse response) {
		Response rsp = Response.valueOf(ModuleName.POWER, PowerCmd.POWER_FIGHT, response.getBytes());
		getInstance().broadcast.push(actorId, rsp);
	}
	
	/**
	 * 推送跨天
	 */
	public static void pushPowerReset(long actorId){
		Response rsp = Response.valueOf(ModuleName.POWER, PowerCmd.PUSH_POWER_RESET);
		getInstance().broadcast.push(actorId, rsp);
	}
	
//	/**
//	 * 推送所有在线，排名有变更重推一次排名
//	 * @param powerList
//	 */
//	public static void pushRankChange(List<PowerVO> powerList) {
//		RankChangeResponse packet = new RankChangeResponse(powerList);
//		Response response = Response.valueOf(ModuleName.POWER, PowerCmd.PUSH_RANK_CHANGE, packet.getBytes());
//		getInstance().broadcast.push2AllOnline(response);
//	}
	
//	/**
//	 * 推送奖励的魂魄id
//	 * @param heroSoulId
//	 */
//	public static void pushNewReward(long actorId, int heroSoulId) {
//		PowerRewardResponse packet = new PowerRewardResponse(heroSoulId);
//		Response response = Response.valueOf(ModuleName.POWER, PowerCmd.PUSH_NEXT_REWARD, packet.getBytes());
//		getInstance().broadcast.push(actorId, response);
//	}

//	public static void pushChallengeFail(long actorId, Result result) {
//		Response response = Response.valueOf(ModuleName.POWER, PowerCmd.CHALLENGE, result.statusCode);
//		getInstance().broadcast.push(actorId, response);
//	}
	
	
}
