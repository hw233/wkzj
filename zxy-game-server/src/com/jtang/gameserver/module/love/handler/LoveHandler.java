package com.jtang.gameserver.module.love.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.love.facade.LoveFacade;
import com.jtang.gameserver.module.love.facade.LoveFightFacade;
import com.jtang.gameserver.module.love.facade.LoveMonsterFacade;
import com.jtang.gameserver.module.love.facade.LoveShopFacade;
import com.jtang.gameserver.module.love.handler.request.AcceptMarryRequest;
import com.jtang.gameserver.module.love.handler.request.FightVideoRequest;
import com.jtang.gameserver.module.love.handler.request.LoveFightRequest;
import com.jtang.gameserver.module.love.handler.request.LoveMonsterRequest;
import com.jtang.gameserver.module.love.handler.request.LoveShopRequest;
import com.jtang.gameserver.module.love.handler.request.MarryRequest;
import com.jtang.gameserver.module.love.handler.response.FightVideoResponse;
import com.jtang.gameserver.module.love.handler.response.GetMarryGiftResponse;
import com.jtang.gameserver.module.love.handler.response.LoveFightInfoResponse;
import com.jtang.gameserver.module.love.handler.response.LoveInfoResponse;
import com.jtang.gameserver.module.love.handler.response.LoveMonsterInfoResponse;
import com.jtang.gameserver.module.love.handler.response.LoveRankInfoResponse;
import com.jtang.gameserver.module.love.handler.response.LoveRankListResponse;
import com.jtang.gameserver.module.love.handler.response.LoveShopInfoResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class LoveHandler extends GatewayRouterHandlerImpl {
	@Autowired
	private PlayerSession playerSession;
	
	@Autowired
	private LoveFacade loveFacde;
	@Autowired
	private LoveFightFacade loveFightFacade;
	@Autowired
	private LoveMonsterFacade loveMonsterFacade;
	@Autowired
	private LoveShopFacade loveShopFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.LOVE;
	}
	@Cmd(Id = LoveCmd.GET_LOVE_INFO)
	public void getLoveInfo(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		LoveInfoResponse loveInfoResponse = loveFacde.getLoveInfo(actorId);
		sessionWrite(session, response, loveInfoResponse);
	}
	@Cmd(Id = LoveCmd.MARRY)
	public void marry(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		MarryRequest marryRequest = new MarryRequest(bytes);
		
		Result result = loveFacde.marry(actorId, marryRequest.targetActorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	@Cmd(Id = LoveCmd.ACCEPT_MARRAY)
	public void acceptMarry(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		AcceptMarryRequest acceptMarryRequest = new AcceptMarryRequest(bytes);
		TResult<LoveInfoResponse> result = loveFacde.acceptMarry(actorId, acceptMarryRequest.accept, acceptMarryRequest.targetActorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			sessionWrite(session, response, result.item);
		}
	}
	@Cmd(Id = LoveCmd.UN_MARRY)
	public void unMarry(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		
		TResult<LoveInfoResponse> result = loveFacde.unMarry(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			sessionWrite(session, response, result.item);
		}
	}
	@Cmd(Id = LoveCmd.GIVE_GIFT)
	public void giveGift(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		
		Result result = loveFacde.giveGift(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	@Cmd(Id = LoveCmd.ACCEPT_GIFT)
	public void acceptGift(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		
		TResult<GetMarryGiftResponse> result = loveFacde.acceptGift(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			sessionWrite(session, response, result.item);
		}
			
	}
	
	@Cmd(Id = LoveCmd.GET_RANK_INFO)
	public void getLoveRankInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<LoveRankInfoResponse> result = loveFightFacade.getInfo(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}else{
			sessionWrite(session, response,result.item);
		}
	}

	@Cmd(Id = LoveCmd.LOVE_FIGHT)
	public void loveFight(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		LoveFightRequest request = new LoveFightRequest(bytes);
		Result result = loveFightFacade.loveFight(actorId,request.targetActorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
		
	}
	
	@Cmd(Id = LoveCmd.BUY_FIGHT_NUM)
	public void buyFightNum(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Result result = loveFightFacade.buyFightNum(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);	
	}
	
	@Cmd(Id = LoveCmd.GET_TOP_RANK)
	public void getTopRank(IoSession session,byte[] bytes,Response response){
		TResult<LoveRankListResponse> result = loveFightFacade.getTopRank();
		sessionWrite(session, response,result.item);
	}
	
	@Cmd(Id = LoveCmd.GET_FIGHT_RANK)
	public void getFightRank(IoSession session,byte[] bytes,Response response){
		long actorId = playerSession.getActorId(session);
		TResult<LoveRankListResponse> result = loveFightFacade.getFightRank(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}else{
			sessionWrite(session, response,result.item);
		}
	}
	
	@Cmd(Id = LoveCmd.LOVE_FIGHT_INFO)
	public void getFightInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<LoveFightInfoResponse> result = loveFightFacade.getFightInfo(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}else{
			sessionWrite(session, response,result.item);
		}
	}
	
	@Cmd(Id = LoveCmd.GET_FIGHT_VIDEO)
	public void getFightVideo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		FightVideoRequest request = new FightVideoRequest(bytes);
		TResult<FightVideoResponse> result = loveFightFacade.getFightVideo(actorId,request.fightVideoId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}else{
			sessionWrite(session, response,result.item);
		}
	}
	
	@Cmd(Id = LoveCmd.GET_LOVE_MONSTER_INFO)
	public void getLoveMonsterInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<LoveMonsterInfoResponse> result = loveMonsterFacade.getInfo(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}else{
			sessionWrite(session, response, result.item);
		}
	}
	
	@Cmd(Id = LoveCmd.LOVE_MONSTER_FIGHT)
	public void loveMonsterFight(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		LoveMonsterRequest request = new LoveMonsterRequest(bytes);
		Result result = loveMonsterFacade.startFight(actorId,request.id);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id = LoveCmd.LOVE_MONSTER_UNLOCK)
	public void unLockBoss(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		LoveMonsterRequest request = new LoveMonsterRequest(bytes);
		Result result = loveMonsterFacade.unLockBoss(actorId,request.id);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = LoveCmd.FLUSH_MONSTER_FIGHT)
	public void flushFightNum(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		LoveMonsterRequest request = new LoveMonsterRequest(bytes);
		Result result = loveMonsterFacade.flushFight(actorId,request.id);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = LoveCmd.LOVE_MONSTER_REWARD)
	public void getReward(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		LoveMonsterRequest request = new LoveMonsterRequest(bytes);
		Result result = loveMonsterFacade.getReward(actorId,request.id);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id=LoveCmd.LOVE_SHOP_INFO)
	public void shopInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<LoveShopInfoResponse> result = loveShopFacade.getInfo(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}else{
			sessionWrite(session, response, result.item);
		}
	}
	
	@Cmd(Id=LoveCmd.LOVE_SHOP_BUY)
	public void shopBuy(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		LoveShopRequest request = new LoveShopRequest(bytes);
		Result result = loveShopFacade.shopBuy(actorId,request.shopId,request.num);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id=LoveCmd.LOVE_SHOP_FLUSH)
	public void shopFlush(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<LoveShopInfoResponse> result = loveShopFacade.shopFlush(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
}
