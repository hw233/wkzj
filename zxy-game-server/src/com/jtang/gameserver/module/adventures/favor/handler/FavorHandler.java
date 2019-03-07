package com.jtang.gameserver.module.adventures.favor.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.favor.facade.FavorFacade;
import com.jtang.gameserver.module.adventures.favor.handler.request.PrivilegeUseRequest;
import com.jtang.gameserver.module.adventures.favor.handler.response.FavorResponse;
import com.jtang.gameserver.module.adventures.favor.handler.response.PrivilegeUseResponse;
import com.jtang.gameserver.module.adventures.favor.model.FavorVO;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class FavorHandler extends GatewayRouterHandlerImpl{
    @Autowired
	private FavorFacade favorFacade;
	@Override
	public byte getModule() {
		return ModuleName.FAVOR;
	}
	
	@Cmd(Id = FavorCmd.GET_INFO)
	public void getFavor(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<FavorVO> result = favorFacade.get(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);	
		}
		else{
			FavorResponse packet = new FavorResponse(result.item);
			sessionWrite(session, response, packet);
		}
	}
	@Cmd(Id = FavorCmd.USE_PRIVILEGE)
	public void usePrivilege(IoSession session, byte[] bytes, Response response){
		PrivilegeUseRequest request = new PrivilegeUseRequest(bytes);
		long actorId = playerSession.getActorId(session);
		TResult<FavorVO> result = favorFacade.usePrivilege(actorId, request.privilegeId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			PrivilegeUseResponse privilegeUseResponse = new PrivilegeUseResponse(result.item);
			sessionWrite(session, response, privilegeUseResponse);
		}
	}
}
