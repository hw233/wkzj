package com.jtang.gameserver.module.enhanced.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.module.enhanced.facade.EnhancedFacade;
import com.jtang.gameserver.module.enhanced.handler.request.EquipUpgradeRequest;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
@Component
public class EnhancedHandler extends GatewayRouterHandlerImpl{

	@Autowired
	EnhancedFacade enhancedFacade;
	@Override
	public byte getModule() {
		return ModuleName.ENHANCED;
	}
//	@Cmd(Id = EnhancedCmd.GET_ENHANCED_INFO)
//	public void getEnhanced(IoSession session, byte[] bytes, Response response){
//		long actorId = playerSession.getActorId(session);
//		Enhanced enhanced = enhancedFacade.get(actorId);
//		if(enhanced == null){
//			response.setStatusCode(GameStatusCodeConstant.ENHANCED_NOT_EXISTS);
//			sessionWrite(session, response);
//			return;
//		}
//		EnhancedResponse packet = new EnhancedResponse(enhanced.level);
//		sessionWrite(session, response, packet);
//	}
//	
//	@Cmd(Id = EnhancedCmd.ENHANCED_UPGRADE)
//	public void upgradeEnhanced(IoSession session, byte[] bytes, Response response){
//		long actorId = playerSession.getActorId(session);
//		TResult<Integer> result = enhancedFacade.upgrade(actorId);
//		if(result.isFail()){
//			response.setStatusCode(result.statusCode);
//			sessionWrite(session, response);
//		}
//		else{
//			EnhancedResponse packet = new EnhancedResponse(result.item);
//			sessionWrite(session, response, packet);
//		}
//	}
	@Cmd(Id = EnhancedCmd.ENHANCE_EQUIP)
	public void upgradeEquip(IoSession session, byte[] bytes, Response response){
		EquipUpgradeRequest request = new EquipUpgradeRequest(bytes);
		long actorId = playerSession.getActorId(session);
		Result result = enhancedFacade.enhanceEquip(actorId, request.equipUuid, request.upgradeNum);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
}
