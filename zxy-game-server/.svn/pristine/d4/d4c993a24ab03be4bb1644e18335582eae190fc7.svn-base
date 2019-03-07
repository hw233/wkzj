package com.jtang.gameserver.module.icon.hander;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.icon.facade.IconFacade;
import com.jtang.gameserver.module.icon.hander.request.IconRequest;
import com.jtang.gameserver.module.icon.hander.request.IconVORequest;
import com.jtang.gameserver.module.icon.hander.request.SexRequest;
import com.jtang.gameserver.module.icon.hander.response.IconResponse;
import com.jtang.gameserver.module.icon.hander.response.IconVOResponse;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class IconHandler extends GatewayRouterHandlerImpl {

	@Autowired
	PlayerSession playerSession;
	
	@Autowired
	IconFacade iconFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.ICON;
	}

	@Cmd(Id = IconCmd.ICON_INFO)
	public void getIconInfo(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<IconResponse> result = iconFacade.getIconInfo(actorId);
		sessionWrite(session, response,result.item);
	}
	
	@Cmd(Id = IconCmd.SET_ICON)
	public void setIcon(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		IconRequest request = new IconRequest(bytes);
		Result result = iconFacade.setIcon(actorId,request.iocn);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = IconCmd.SET_FRAM)
	public void setFram(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		IconRequest request = new IconRequest(bytes);
		Result result = iconFacade.setFram(actorId,request.iocn);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = IconCmd.GET_ICON_VO)
	public void getIconVO(IoSession session, byte[] bytes, Response response) {
		IconVORequest request = new IconVORequest(bytes);
		IconVO iconVO = iconFacade.getIconVO(request.actorId);
		IconVOResponse rsp = new IconVOResponse(iconVO);
		sessionWrite(session, response, rsp);
	}
	@Cmd(Id = IconCmd.MODIFY_SEX)
	public void actorRename(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		SexRequest sexRequest = new SexRequest(bytes);
		Result result = iconFacade.setSex(actorId, sexRequest.sex);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
}
