package com.jtang.gameserver.module.snatch.helper;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.dataconfig.service.SnatchService;
import com.jtang.gameserver.dbproxy.entity.Snatch;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.snatch.facade.SnatchFacade;
import com.jtang.gameserver.module.snatch.handler.SnatchCmd;
import com.jtang.gameserver.module.snatch.handler.response.ExchangeListResponse;
import com.jtang.gameserver.module.snatch.handler.response.PushAchimentResponse;
import com.jtang.gameserver.module.snatch.handler.response.PushSnatchAttributeResponse;
import com.jtang.gameserver.module.snatch.handler.response.SnatchFightNumResponse;
import com.jtang.gameserver.module.snatch.handler.response.SnatchNumResponse;
import com.jtang.gameserver.module.snatch.handler.response.StartSnatchResultResponse;
import com.jtang.gameserver.module.snatch.model.SnatchVO;
import com.jtang.gameserver.server.broadcast.Broadcast;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class SnatchPushHelper {
	protected static final Logger LOGGER = LoggerFactory.getLogger(SnatchPushHelper.class);
	
	private static ObjectReference<SnatchPushHelper> REF = new ObjectReference<>();
	
	@Autowired
	private PlayerSession playerSession;
	
	@Autowired
	private Broadcast broadcast;
	
	@Autowired
	private SnatchFacade snatchFacade;

	@PostConstruct
	protected void init() {
		REF.set(this);
	}
	
	private static SnatchPushHelper getInstance(){
		return REF.get();
	}
	
	/**
	 * 推送抢夺战斗结果
	 * @param battleResult
	 */
	public static void pushBattleResult(long actorId, FightData fightData, SnatchVO snatchVO,List<RewardObject> rewardList,int snatchNum) {
		StartSnatchResultResponse packet = new StartSnatchResultResponse(fightData, snatchVO, rewardList,snatchNum);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("\n" + packet.format() + "\n");
		}
		Response rsp = Response.valueOf(ModuleName.SNATCH, SnatchCmd.START_SNATCH, packet.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	/**
	 * 抢夺战斗结果处理失败,同步给客户端
	 * @param actorId
	 * @param statusCode
	 */
	public static void pushSnatchFail(long actorId, short statusCode) {
		Response rsp = Response.valueOf(ModuleName.SNATCH, SnatchCmd.START_SNATCH);
		rsp.setStatusCode(statusCode);
		getInstance().playerSession.push(actorId, rsp);
	}
	
	/**
	 * 推送抢夺属性更新
	 * @param actorId
	 * @param map
	 */
	public static void pushAchimentProgress(long actorId, Map<Integer, Integer> attrMap) {
		PushAchimentResponse res = new PushAchimentResponse(attrMap);
		Response rsp = Response.valueOf(ModuleName.SNATCH, SnatchCmd.PUSH_ACHIMENTS, res.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

	public static void pushExchangeResponse(Long actorId,ExchangeListResponse response) {
		Response rsp = Response.valueOf(ModuleName.SNATCH, SnatchCmd.GET_EXCHANGE_LIST,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushActorScore(long actorId,int score){
		PushSnatchAttributeResponse response = new PushSnatchAttributeResponse(score);
		Response rsp = Response.valueOf(ModuleName.SNATCH, SnatchCmd.PUSH_SNATCH_ATTR,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushBuyInfo(long actorId,int costTicket,int buySnatchNum,int snatchNum){
		SnatchNumResponse response = new SnatchNumResponse(costTicket,buySnatchNum,snatchNum);
		Response rsp = Response.valueOf(ModuleName.SNATCH, SnatchCmd.PUSH_BUY_INFO,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushFightNum(Long actorId, Snatch snatch) {
		SnatchFightNumResponse response = new SnatchFightNumResponse();
		response.fightNum = snatch.snatchNum;
		int nextFlushTime = snatch.flushFightTime + SnatchService.get().flushTime;
		response.flushTime = nextFlushTime;
		Response rsp = Response.valueOf(ModuleName.SNATCH, SnatchCmd.PUSH_FIGHT_NUM,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
}
