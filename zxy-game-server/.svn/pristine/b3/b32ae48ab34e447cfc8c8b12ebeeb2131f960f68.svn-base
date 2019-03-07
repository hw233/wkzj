package com.jtang.gameserver.admin.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.GameAdminModule;
import com.jtang.gameserver.admin.facade.HeroMaintianFacade;
import com.jtang.gameserver.admin.handler.request.AddAllHeroSoulRequest;
import com.jtang.gameserver.admin.handler.request.AddHeroExpRequest;
import com.jtang.gameserver.admin.handler.request.AddHeroSoulRequest;
import com.jtang.gameserver.admin.handler.request.DeleteHeroRequest;
import com.jtang.gameserver.admin.handler.request.DeleteHeroSoulRequest;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;

@Component
public class HeroMaintianHandler extends AdminRouterHandlerImpl {

	protected final Log LOGGER = LogFactory.getLog(HeroMaintianHandler.class);
	
	@Autowired
	HeroMaintianFacade heroMaintianFacade;

	@Override
	public byte getModule() {
		return GameAdminModule.HERO;
	}

	@Cmd(Id = HeroMaintianCmd.DEL_HERO)
	public void deleteHero(IoSession session, byte[] bytes, Response response) {
		DeleteHeroRequest delHero = new DeleteHeroRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+delHero.actorId+"-------- heroId = "+delHero.heroId);
		}
		Result result = heroMaintianFacade.deleteHero(delHero.actorId, delHero.heroId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

	@Cmd(Id = HeroMaintianCmd.ADD_HERO_EXP)
	public void addHeroExp(IoSession session, byte[] bytes, Response response) {
		AddHeroExpRequest addHeroExp = new AddHeroExpRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+addHeroExp.actorId+"-------- heroId = "+addHeroExp.heroId+" ---- exp = "+addHeroExp.exp);
		}
		Result result = heroMaintianFacade.addHeroExp(addHeroExp.actorId, addHeroExp.heroId, addHeroExp.exp);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = HeroMaintianCmd.ADD_HERO_SOUL)
	public void addHeroSoul(IoSession session, byte[] bytes, Response response){
		AddHeroSoulRequest addHeroSoul = new AddHeroSoulRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+addHeroSoul.actorId+"-------- heroId = "+addHeroSoul.heroId+" ---- num = "+addHeroSoul.num);
		}
		Result result = heroMaintianFacade.addHeroSoul(addHeroSoul.actorId,addHeroSoul.heroId,addHeroSoul.num);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = HeroMaintianCmd.DEL_HERO_SOUL)
	public void delHeroSoul(IoSession session, byte[] bytes, Response response){
		DeleteHeroSoulRequest delHeroSoul = new DeleteHeroSoulRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+delHeroSoul.actorId+"-------- heroId = "+delHeroSoul.heroId+" ---- num = "+delHeroSoul.num);
		}
		Result result = heroMaintianFacade.deleteHeroSoul(delHeroSoul.actorId, delHeroSoul.heroId,delHeroSoul.num);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response); 
	}

	@Cmd(Id = HeroMaintianCmd.ADD_ALL_HERO_SOUL)
	public void addAllHeroSoul(IoSession session, byte[] bytes, Response response){
		AddAllHeroSoulRequest addAllHeroSoul = new AddAllHeroSoulRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+addAllHeroSoul.actorId+ " add all HeroSoul ");
		}
		Result result = heroMaintianFacade.addAllHeroSoul(addAllHeroSoul.actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

}
