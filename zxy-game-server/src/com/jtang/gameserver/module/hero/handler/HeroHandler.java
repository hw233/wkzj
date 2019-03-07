package com.jtang.gameserver.module.hero.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.HeroVO;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.buffer.facade.BufferFacade;
import com.jtang.gameserver.module.buffer.model.HeroBuffer;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.handler.request.BreakThroughRequest;
import com.jtang.gameserver.module.hero.handler.request.Soul2HeroRequest;
import com.jtang.gameserver.module.hero.handler.response.HeroListResponse;
import com.jtang.gameserver.module.hero.handler.response.HeroResponse;
import com.jtang.gameserver.module.hero.handler.response.HeroSoulListResponse;
import com.jtang.gameserver.module.hero.handler.response.Soul2HeroResponse;
import com.jtang.gameserver.module.hero.model.HeroSoulVO;
import com.jtang.gameserver.module.hero.model.HeroUnit;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

/**
 * 仙人模块对外处理类
 * 
 * @author vinceruan
 * 
 */
@Component
public class HeroHandler extends GatewayRouterHandlerImpl {
	
	@Autowired
	HeroFacade heroFacade;
	
	@Autowired
	HeroSoulFacade heroSoulFacade;
	
	@Autowired
	BufferFacade bufferFacade;

	@Override
	public byte getModule() {
		return ModuleName.HERO;
	}

	/**
	 * 拉取仙人分页列表
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = HeroCmd.LOAD_HERO_LIST)
	public void loadHeroList(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session); 
		
		Collection<HeroVO> list = heroFacade.getList(actorId);

		List<HeroResponse> resList = new ArrayList<>();

		for (HeroVO hero : list) {
			List<BufferVO> heroBufferList = new ArrayList<>();
			HeroBuffer buf = bufferFacade.getHeroBuffer(actorId, hero.getHeroId());
			if (buf != null) {
				for (List<BufferVO> lt : buf.bufferTypeMap.values()) {
					heroBufferList.addAll(lt);
				}
			}
			resList.add(HeroResponse.valueOf(hero, heroBufferList));
		}
		int composeNum = heroFacade.getComposeNum(actorId);
		HeroListResponse res = HeroListResponse.valueOf(resList, composeNum);
				
		sessionWrite(session, response, res);
	}
	
	/**
	 * 获取仙人魂魄分页列表
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = HeroCmd.LOAD_HERO_SOUL_LIST)
	public void loadHeroSoulList(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		
		List<HeroSoulVO> list = heroSoulFacade.getList(actorId);
		HeroSoulListResponse res = HeroSoulListResponse.valueOf(list);		
		sessionWrite(session, response, res);
	}
	
	/**
	 * 魂魄转仙人(招募)
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = HeroCmd.SOUL2HERO)
	public void soul2Hero(IoSession session, byte[] bytes, Response response) {		
		long actorId = playerSession.getActorId(session);
		Soul2HeroRequest request = new Soul2HeroRequest(bytes);
		TResult<HeroUnit> tResult = heroFacade.soul2Hero(actorId, request.heroId);
		
		if (tResult.isFail()) {
			response.setStatusCode(tResult.statusCode);
			sessionWrite(session, response);
			return;
		}
		Soul2HeroResponse packet = new Soul2HeroResponse(tResult.item);
		sessionWrite(session, response, packet);
	}
	
	/**
	 * 突破
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = HeroCmd.BREAK_THROUGH)
	public void breakThrough(IoSession session, byte[] bytes, Response response) {
		BreakThroughRequest request = new BreakThroughRequest(bytes);
		long actorId = playerSession.getActorId(session);
		Result result = heroFacade.breakThrough(actorId, request.heroId);
		
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
		return;
	}
}
