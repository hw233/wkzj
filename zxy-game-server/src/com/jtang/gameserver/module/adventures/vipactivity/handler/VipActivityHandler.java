package com.jtang.gameserver.module.adventures.vipactivity.handler;

import static com.jiatang.common.GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.vipactivity.facade.EquipComposeFacade;
import com.jtang.gameserver.module.adventures.vipactivity.facade.GiveEquipFacade;
import com.jtang.gameserver.module.adventures.vipactivity.facade.HeroComposeFacade;
import com.jtang.gameserver.module.adventures.vipactivity.facade.MainHeroFacade;
import com.jtang.gameserver.module.adventures.vipactivity.facade.ResetHeroEquipFacade;
import com.jtang.gameserver.module.adventures.vipactivity.handler.request.EquipComposeRequest;
import com.jtang.gameserver.module.adventures.vipactivity.handler.request.GiveEquipRequest;
import com.jtang.gameserver.module.adventures.vipactivity.handler.request.HeroComposeRequest;
import com.jtang.gameserver.module.adventures.vipactivity.handler.request.ResetEquipRequest;
import com.jtang.gameserver.module.adventures.vipactivity.handler.request.ResetHeroRequest;
import com.jtang.gameserver.module.adventures.vipactivity.handler.request.SetMainHeroRequest;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.EquipComposeInfoResponse;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.EquipComposeResultResponse;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.GiveEquipInfoResponse;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.GiveEquipResponse;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.HeroComposeResultResponse;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.MainHeroResponse;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.VipActivityInfoResponse;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.model.VipPrivilege;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class VipActivityHandler extends GatewayRouterHandlerImpl {

//	@Autowired
//	private ActorResetFacade actorResetFacade;
	@Autowired
	MainHeroFacade mainHeroFacade;
	@Autowired
	EquipComposeFacade equipComposeFacade;
	@Autowired
	HeroComposeFacade  heroComposeFacade;
	@Autowired
	EquipFacade equipFacade;
	@Autowired
	VipFacade vipFacade;
	@Autowired
	GiveEquipFacade giveEquipFacade;
	@Autowired
	ResetHeroEquipFacade resetheroEquipFacade;
	@Autowired
	HeroFacade heroFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.VIPACTIVITY;
	}
	
	@Cmd(Id = VipActivityCmd.MAIN_HERO_INFO)
	public void mainHeroInfo(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<MainHeroResponse> result = mainHeroFacade.getMainHeroInfo(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);			
			return;
		}
		sessionWrite(session, response, result.item);
	}
	
	@Cmd(Id = VipActivityCmd.MAIN_HERO_SET)
	public void mainHeroSet(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		SetMainHeroRequest request = new SetMainHeroRequest(bytes);
		boolean useGold = request.useGold == 1;
		TResult<MainHeroResponse> result = mainHeroFacade.setMainHero(actorId, request.heroId, useGold);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
			return;
		}
		sessionWrite(session, response, result.item);
	}

	
	@Cmd(Id = VipActivityCmd.EQUIP_COMPOSE_INFO)
	public void equipComposeInfo(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);

		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
		// TODO V5开启炼器宗师
		if (vipPrivilege == null || vipPrivilege.addComposeEquipNum == 0) {
			response.setStatusCode(VIP_LEVEL_NO_ENOUGH);
			sessionWrite(session, response);
			return;
		}
		
		boolean canCompose = equipFacade.canCompose(actorId, vipPrivilege.addComposeEquipNum);

		EquipComposeInfoResponse packet = new EquipComposeInfoResponse(canCompose);
		sessionWrite(session, response, packet);
	}
	
	@Cmd(Id = VipActivityCmd.EQUIP_COMPOSE)
	public void equipCompose(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		EquipComposeRequest request = new EquipComposeRequest(bytes);
		TResult<EquipComposeResultResponse> result = equipComposeFacade.compose(actorId, request.equipType, request.isUseTicket(), request.uuidList);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
			return;
		}
		
		sessionWrite(session, response, result.item);
	}
	
	@Cmd(Id = VipActivityCmd.HERO_COMPOSE)
	public void heroCompose(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		HeroComposeRequest request = new HeroComposeRequest(bytes);
		TResult<HeroComposeResultResponse> result = heroComposeFacade.compose(actorId, request.isUseTicket(), request.heroIdList);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
			return;
		}
		sessionWrite(session, response, result.item);
	}
	
	@Cmd(Id = VipActivityCmd.ACTOR_RESET)
	public void actorReset(IoSession session, byte[] bytes, Response response) {
//		long actorId = playerSession.getActorId(session);
//		Result result = actorResetFacade.resetActor(actorId);
//		if (result.isFail()) {
//			response.setStatusCode(result.statusCode);
//		}
//		sessionWrite(session, response);
	}
	
	@Cmd(Id = VipActivityCmd.GIVE_EQUIP_ALLY)
	public void giveEquip(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		long otherId = new GiveEquipRequest(bytes).targetActor;
		TResult<Integer> result = giveEquipFacade.giveEquip(actorId,otherId);
		if(result.isOk()){
			response.setValue(new GiveEquipResponse(result.item).getBytes());
		}
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = VipActivityCmd.GIVE_EQUIP_INFO)
	public void giveEquipInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<GiveEquipInfoResponse> result = giveEquipFacade.giveEquipInfo(actorId);
		GiveEquipInfoResponse treasureInfo = null;
		if(result.item!=null){
			treasureInfo = result.item;
		}else{
			treasureInfo = new GiveEquipInfoResponse();
		}
		response.setValue(treasureInfo.getBytes());
		sessionWrite(session, response);
	}
	
	@Cmd(Id = VipActivityCmd.RESET_EQUIP)
	public void resetEquip(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		ResetEquipRequest request = new ResetEquipRequest(bytes);
		Result result = resetheroEquipFacade.resetEquip(actorId,request.uuid);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session,response);
	}
	
	@Cmd(Id = VipActivityCmd.RESET_HERO)
	public void resetHero(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		ResetHeroRequest request = new ResetHeroRequest(bytes);
		Result result = resetheroEquipFacade.resetHero(actorId,request.heroId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session,response);
	}
	
	@Cmd(Id = VipActivityCmd.GET_INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		int heroComposeNum = heroFacade.getComposeNum(actorId);
		int equipComposeNum = equipFacade.getComposeNum(actorId);
		int heroResetNum = heroFacade.getResetNum(actorId);
		int equipResetNum = equipFacade.getResetNum(actorId);
		VipActivityInfoResponse rsp = new VipActivityInfoResponse(equipComposeNum,heroComposeNum,heroResetNum + equipResetNum);
		sessionWrite(session, response,rsp);
	}
	
}
