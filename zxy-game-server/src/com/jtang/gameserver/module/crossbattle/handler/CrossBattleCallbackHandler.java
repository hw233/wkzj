package com.jtang.gameserver.module.crossbattle.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.ModuleName;
import com.jiatang.common.crossbattle.CrossBattleCmd;
import com.jiatang.common.crossbattle.response.ActorCrossDataW2G;
import com.jiatang.common.crossbattle.response.AllEndW2G;
import com.jiatang.common.crossbattle.response.AttackPlayerW2G;
import com.jiatang.common.crossbattle.response.EndRewardW2G;
import com.jiatang.common.crossbattle.response.ExchangePointW2G;
import com.jiatang.common.crossbattle.response.LineupW2G;
import com.jiatang.common.crossbattle.response.NoticeGameW2G;
import com.jiatang.common.crossbattle.response.SignupW2G;
import com.jiatang.common.model.EquipVO;
import com.jiatang.common.model.HeroAndBuff;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.ActorResponse;
import com.jtang.core.protocol.Response;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.crossbattle.facade.CrossBattleCallbackFacade;
import com.jtang.gameserver.module.crossbattle.handler.response.ActorCrossDataResponse;
import com.jtang.gameserver.module.crossbattle.helper.CrossBattleClientPushHelper;
import com.jtang.gameserver.module.crossbattle.model.CrossDataVO;
import com.jtang.gameserver.module.hero.handler.response.HeroResponse;
import com.jtang.gameserver.module.lineup.handler.response.ViewLineupResponse;
import com.jtang.gameserver.server.session.PlayerSession;
import com.jtang.gameserver.worldclient.router.WorldClientRouterHandlerImpl;
import com.jtang.gameserver.worldclient.session.WorldClientSession;

/**
 * worldserver返回的消息由该handler类接收
 * 
 * @author 0x737263
 * 
 */
@Component
public class CrossBattleCallbackHandler extends WorldClientRouterHandlerImpl {

	@Autowired
	private WorldClientSession worldClientSession;

	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private CrossBattleCallbackFacade crossBattleCallbackFacade;

	@Override
	public byte getModule() {
		return ModuleName.CROSS_BATTLE;
	}

	@Cmd(Id = CrossBattleCmd.W2G_SIGN_UP)
	public void signupResult(IoSession session, ActorResponse response) {
		SignupW2G signupResponse = new SignupW2G(response.getValue());
		crossBattleCallbackFacade.getSignupData(signupResponse.powerRank);
	}

	@Cmd(Id = CrossBattleCmd.W2G_NOTICE_GAME)
	public void noticeGame(IoSession session, ActorResponse response) {
		NoticeGameW2G noticeGameW2G = new NoticeGameW2G(response.getValue());
		crossBattleCallbackFacade.setCrossBattleState(noticeGameW2G.start);
		CrossBattleClientPushHelper.pushNoticeGame(noticeGameW2G);
	}

	@Cmd(Id = CrossBattleCmd.G2W_POST_BATTLE_RESULT)
	public void attackPlayerBattleResult(IoSession session, ActorResponse response) {
//		AttackPlayerResultW2G attackPlayerResponse = new AttackPlayerResultW2G(response.getValue());
		BattleResult result = crossBattleCallbackFacade.pickBattleResult(response.getActorId());
		if (response.getStatusCode() == StatusCode.SUCCESS) {
			CrossBattleClientPushHelper.pushBattleResult(response.getActorId(), result.fightData);
		} else {
			Response res = Response.valueOf(ModuleName.CROSS_BATTLE, CrossBattleCmd.ATTACK_ACTOR);
			res.setStatusCode(response.getStatusCode());
			playerSession.push(response.getActorId(), res);
		}
	}

	@Cmd(Id = CrossBattleCmd.GET_CROSS_DATA)
	public void getCrossData(IoSession session, ActorResponse response) {
		ActorCrossDataW2G actorCrossDataW2G = new ActorCrossDataW2G(response.getValue());
		CrossDataVO crossDataVO = new CrossDataVO(actorCrossDataW2G.crossData);
		ActorCrossDataResponse actorCrossDataResponse = new ActorCrossDataResponse(crossDataVO );
		response.setValue(actorCrossDataResponse.getBytes());
		playerSession.push(response.getActorId(), response);
	}
	@Cmd(Id = CrossBattleCmd.W2G_PUSH_DAY_END_REWARD)
	public void pushDayEndReward(IoSession session, ActorResponse response) {
		EndRewardW2G endRewardW2G = new EndRewardW2G(response.getValue());
		Result result = crossBattleCallbackFacade.dayBattleEndRewardResult(response.getActorId(), endRewardW2G.endReward);
		Response resultResponse = Response.valueOf(getModule(), CrossBattleCmd.DAY_BATTLE_END_REWARD_RESULT);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
//			DayBattleEndRewardResponse dayRewardResponse = new DayBattleEndRewardResponse(endRewardW2G.endReward);
			response.setValue(response.getValue());
		}
		resultResponse.setValue(response.getValue());
		playerSession.push(response.getActorId(), resultResponse);
	}

	@Cmd(Id = CrossBattleCmd.GET_LINEUP)
	public void getLineup(IoSession session, ActorResponse response) {
		LineupW2G lineupW2G = new LineupW2G(response.getValue());
		Map<Integer, List<EquipVO>> lineupEquips = lineupW2G.viewLineupVO.lineupEquips;
		Map<Integer, HeroResponse> lineupHeros = new HashMap<>();
		for (Entry<Integer, HeroAndBuff> entry : lineupW2G.viewLineupVO.lineupHeros.entrySet()) {
			int key = entry.getKey();
			HeroAndBuff heroAndBuff = entry.getValue();
			HeroResponse heroResponse = new HeroResponse();
			heroResponse.buffList = heroAndBuff.buffList;
			heroResponse.hero = heroAndBuff.hero;
			lineupHeros.put(key, heroResponse);
		}
		ViewLineupResponse viewLineupResponse = new ViewLineupResponse(lineupHeros, lineupEquips);
		response.setValue(viewLineupResponse.getBytes());
		playerSession.push(response.getActorId(), response);
	}

	@Cmd(Id = CrossBattleCmd.ATTACK_ACTOR)
	public void attackPlayerResult(IoSession session, ActorResponse response) {
		if (response.getStatusCode() == StatusCode.SUCCESS) {
			AttackPlayerW2G attackPlayerResponse = new AttackPlayerW2G(response.getValue());
			byte[] selfFightModel = attackPlayerResponse.selfLineupFightModel;
			byte[] targetFightModel = attackPlayerResponse.targetLineupFightModel;
			crossBattleCallbackFacade.attactActorCallBack(response.getActorId(), attackPlayerResponse.targetActorId, selfFightModel,
					targetFightModel, attackPlayerResponse.selfMorale, attackPlayerResponse.targetMorale);
		} else {
			playerSession.push(response.getActorId(), response);
		}
	}

	@Cmd(Id = CrossBattleCmd.GET_SERVER_SCORE_LIST)
	public void getServerRank(IoSession session, ActorResponse response) {
//		ServerRankW2G serverRankW2G = new ServerRankW2G(response.getValue());
//		World2GameForward world2GameForward = new World2GameForward(serverRankW2G.serverRank);
//		response.setValue(world2GameForward.getBytes());
		playerSession.push(response.getActorId(), response);

	}

	@Cmd(Id = CrossBattleCmd.GET_HOME_SERVER_RANK)
	public void getHomeServerRank(IoSession session, ActorResponse response) {
//		HomeServerRankW2G homeServerRankW2G = new HomeServerRankW2G(response.getValue());
//		World2GameForward world2GameForward = new World2GameForward(homeServerRankW2G.homeServerRank);
//		response.setValue(world2GameForward.getBytes());
		playerSession.push(response.getActorId(), response);
	}

	@Cmd(Id = CrossBattleCmd.DAY_BATTLE_END_REWARD_RESULT)
	public void dayBattleEndRewardResult(IoSession session, ActorResponse response) {
		playerSession.push(response.getActorId(), response);
		
	}

	@Cmd(Id = CrossBattleCmd.EXCHANGE_POINT)
	public void exchangePoint(IoSession session, ActorResponse response) {
		ExchangePointW2G exchangePointW2G = new ExchangePointW2G(response.getValue());
		if (response.getStatusCode() == GameStatusCodeConstant.SUCCESS) {
			Result result = crossBattleCallbackFacade.exchangePoint(response.getActorId(), exchangePointW2G.list);
			if(result.isFail()){
				response.setStatusCode(result.statusCode);
			}else{
//				ExchangePointVO exchangenePointVO = new ExchangePointVO(exchangePointW2G.list);
//				ExchangePointResponse exchangePointResponse = new ExchangePointResponse(exchangenePointVO,exchangePointW2G.point);
				response.setValue(response.getValue());
			}
		}
		playerSession.push(response.getActorId(), response);
	}

	@Cmd(Id = CrossBattleCmd.PUSH_ACTOR_ATTRIBUTE_CHANGE)
	public void actorAttributeChange(IoSession session, ActorResponse response) {
//		ActorAttributeChangeW2G actorAttributeChangeW2G = new ActorAttributeChangeW2G(response.getValue());
//		ActorAttributeChangeResponse actorAttributeChangeResponse = new ActorAttributeChangeResponse(actorAttributeChangeW2G.actorAttributeChangeVO);
//		response.setValue(actorAttributeChangeResponse.getBytes());
		playerSession.push(playerSession.onlineActorList(), response);
	}

	@Cmd(Id = CrossBattleCmd.PUSH_SERVER_TOTAL_HURT)
	public void severTotalHurt(IoSession session, ActorResponse response) {
//		TotalHurtW2G totalHurtW2G = new TotalHurtW2G(response.getValue());
//		ServerTotalHurtResponse serverTotalHurtResponse = new ServerTotalHurtResponse(totalHurtW2G.hurtMap);
//		response.setValue(serverTotalHurtResponse.getBytes());
		playerSession.push(playerSession.onlineActorList(), response);
	}
	
	@Cmd(Id = CrossBattleCmd.GET_CROSS_BATTLE_CONFIG)
	public void getCrossBattleConfig(IoSession session, ActorResponse response){
//		CrossBattleConfigW2G crossBattleConfigW2G = new CrossBattleConfigW2G(response.getValue());
//		CrossBattleConfigResponse crossBattleConfigresponse = new CrossBattleConfigResponse();
//		crossBattleConfigresponse.startDate = crossBattleConfigW2G.startDate;
//		crossBattleConfigresponse.endDate = crossBattleConfigW2G.endDate;
//		crossBattleConfigresponse.startTime = crossBattleConfigW2G.startTime;
//		crossBattleConfigresponse.signupTime = crossBattleConfigW2G.signupTime;
//		crossBattleConfigresponse.endTime = crossBattleConfigW2G.endTime;
//		crossBattleConfigresponse.serverIdMap = crossBattleConfigW2G.serverIdMap;
//		crossBattleConfigresponse.readFlag = crossBattleConfigW2G.readFlag;
//		response.setValue(crossBattleConfigresponse.getBytes());
		playerSession.push(response.getActorId(), response);
	}
	
	@Cmd(Id = CrossBattleCmd.GET_LAST_BATTLE_RESULT)
	public void getlastBattleResult(IoSession session, ActorResponse response){
//		LastBattleResultW2G lastBattleResultW2G = new LastBattleResultW2G(response.getValue());
//		LastBattleResultResponse lastBattleResultResponse = new LastBattleResultResponse(lastBattleResultW2G.list);
//		response.setValue(lastBattleResultResponse.getBytes());
		playerSession.push(response.getActorId(), response);
	}
	
	@Cmd(Id = CrossBattleCmd.GET_ACTOR_POINT)
	public void getActorPoint(IoSession session, ActorResponse response){
//		ActorPointW2G actorPointW2G = new ActorPointW2G(response.getValue());
//		ActorPointResponse actorPointResponse = new ActorPointResponse(actorPointW2G.point);
//		response.setValue(actorPointResponse.getBytes());
		playerSession.push(response.getActorId(), response);
	}

	@Cmd(Id = CrossBattleCmd.SYS_END_NOTICE)
	public void endNotice(IoSession session, ActorResponse response){
//		EndNoticeW2G endNoticeW2G = new EndNoticeW2G(response.getValue());
//		EndNoticeResponse endNoticeResponse = new EndNoticeResponse(endNoticeW2G.endNoticeVO);
//		response.setValue(endNoticeResponse.getBytes());
		playerSession.push(playerSession.onlineActorList(), response);
	}
	
	@Cmd(Id = CrossBattleCmd.ALL_END_REWARD)
	public void allEndReward(IoSession session, ActorResponse response){
		AllEndW2G allEndW2G = new AllEndW2G(response.getValue());
		crossBattleCallbackFacade.allEndReward(response.getActorId(),allEndW2G.rewardObjects,allEndW2G.serverScoreRank);
	}
	@Cmd(Id = CrossBattleCmd.SET_READ_FLAG)
	public void setReadFlag(IoSession session, ActorResponse response){
		playerSession.push(response.getActorId(), response);
	}
	@Cmd(Id = CrossBattleCmd.PUSH_ATTACK_NOTICE)
	public void attackNotic(IoSession session, ActorResponse response){
		playerSession.push(response.getActorId(), response);
	}

}
