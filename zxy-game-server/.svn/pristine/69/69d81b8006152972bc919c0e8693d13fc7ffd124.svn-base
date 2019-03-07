package com.jtang.gameserver.module.vampiir.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jiatang.common.model.HeroVO;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ListResult;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.vampiir.facade.VampiirFacade;
import com.jtang.gameserver.module.vampiir.handler.request.DoVampiirRequest;
import com.jtang.gameserver.module.vampiir.handler.request.ExchangeHeroSoulRequest;
import com.jtang.gameserver.module.vampiir.handler.response.DoVampiirResponse;
import com.jtang.gameserver.module.vampiir.handler.response.ExchangeHeroSoulResponse;
import com.jtang.gameserver.module.vampiir.model.ExchangeVO;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class VampiirHandler extends GatewayRouterHandlerImpl {

	@Autowired
	private VampiirFacade vampiirFacade;

	@Override
	public byte getModule() {
		return ModuleName.VAMPIIR;
	}

//	/**
//	 * 获取吸灵室当前等级信息
//	 * 
//	 * @param session
//	 * @param bytes
//	 * @param response
//	 */
//	@Cmd(Id = VampiirCmd.GET_INFO)
//	public void getVampiirInfo(IoSession session, byte[] bytes, Response response) {
//		long actorId = playerSession.getActorId(session);
//		Vampiir vampiir = vampiirFacade.get(actorId);
//		if (vampiir == null) {
//			response.setStatusCode(GameStatusCodeConstant.VAMPIIR_INFO_ERROR);
//			sessionWrite(session, response);
//			return;
//		}
//		VampiirInfoResponse packet = new VampiirInfoResponse(vampiir.level);
//		sessionWrite(session, response, packet);
//	}
//
//	/**
//	 * 吸灵室升级
//	 * 
//	 * @param session
//	 * @param bytes
//	 * @param response
//	 */
//	@Cmd(Id = VampiirCmd.LEVEL_UP)
//	public void upgradeVampiir(IoSession session, byte[] bytes, Response response) {
//		long actorId = playerSession.getActorId(session);
//		TResult<Integer> result = vampiirFacade.upgrade(actorId);
//		if (result.isFail()) {
//			response.setStatusCode(result.statusCode);
//			sessionWrite(session, response);
//		} else {
//			VampiirInfoResponse packet = new VampiirInfoResponse(result.item);
//			sessionWrite(session, response, packet);
//		}
//	}
//
	/**
	 * 执行吸灵
	 * 
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = VampiirCmd.DO_VAMPIIR)
	public void doVampiir(IoSession session, byte[] bytes, Response response) {
		DoVampiirRequest request = new DoVampiirRequest(bytes);
		long actorId = playerSession.getActorId(session);
		TResult<HeroVO> result = vampiirFacade.doVampiir(actorId, request.heroId, request.heroIds, request.heroSouls, request.goods);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
			return;
		}
		DoVampiirResponse packet = new DoVampiirResponse(result.item.getHeroId(), result.item.getLevel(), result.item.exp);
		sessionWrite(session, response, packet);
	}
	
	/**
	 * 兑换桃子
	 */
	@Cmd(Id = VampiirCmd.EXCHANGE_HEROSOUL)
	public void exchange(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		ExchangeHeroSoulRequest request = new ExchangeHeroSoulRequest(bytes);
		ListResult<ExchangeVO> result = vampiirFacade.exchange(actorId,request.heroSouls);
		ExchangeHeroSoulResponse exchangeResponse = new ExchangeHeroSoulResponse(result.item);
		if(result.isOk()){
			response.setValue(exchangeResponse.getBytes());
		}else{
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	
}
