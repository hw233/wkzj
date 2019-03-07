package com.jtang.gameserver.admin.handler;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.admin.GameAdminModule;
import com.jtang.gameserver.admin.facade.ActorMaintianFacade;
import com.jtang.gameserver.admin.handler.request.ActorChangeUidRequest;
import com.jtang.gameserver.admin.handler.request.ActorModifyRequest;
import com.jtang.gameserver.admin.handler.request.AddReputationRequest;
import com.jtang.gameserver.admin.handler.request.DecreaseGoldRequest;
import com.jtang.gameserver.admin.handler.request.DeleteActorActiveRequest;
import com.jtang.gameserver.admin.handler.request.GiveGoldRequest;
import com.jtang.gameserver.admin.handler.request.ModifyVipLevelRequest;
import com.jtang.gameserver.admin.handler.response.ActorModifyResponse;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;
@Component
public class ActorHandler extends AdminRouterHandlerImpl{

	@Autowired
	private ActorMaintianFacade actorModifyFacade;
	@Override
	public byte getModule() {
		return GameAdminModule.ACTOR;
	}
	
	@Cmd(Id = ActorCmd.MODIFY)
	public void modify(IoSession session, byte[] bytes, Response response) {		
		ActorModifyRequest actorModifyRequest = new ActorModifyRequest(bytes);
	    TResult<List<ActorAttributeKey>> result = actorModifyFacade.modify(actorModifyRequest.actorId, actorModifyRequest.map);
		if (result.isFail()){
			response.setStatusCode(result.statusCode);
		} else {
			ActorModifyResponse actorModifyResponse = new ActorModifyResponse(result.item);
			sessionWrite(session, response, actorModifyResponse);
			
		}
		
	}
	
	@Cmd(Id = ActorCmd.ADD_GOLD)
	public void giveGold(IoSession session, byte[] bytes, Response response) {
		GiveGoldRequest giveGoldRequest = new GiveGoldRequest(bytes);
		Result result = actorModifyFacade.addGold(giveGoldRequest.actorId, giveGoldRequest.giveNum);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = ActorCmd.DECREASE_GOLD)
	public void decreaseGold(IoSession session, byte[] bytes, Response response) {
		DecreaseGoldRequest decreaseGoldRequest = new DecreaseGoldRequest(bytes);
		Result result = actorModifyFacade.decreaseGold(decreaseGoldRequest.actorId, decreaseGoldRequest.decreaseNum);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = ActorCmd.GIVE_VIP_LEVEL)
	public void giveVipLevel(IoSession session, byte[] bytes, Response response) { 
		ModifyVipLevelRequest giveVipLevelRequest = new ModifyVipLevelRequest(bytes);
		Result result = actorModifyFacade.modifyVipLevel(giveVipLevelRequest.actorId, giveVipLevelRequest.level);
		if (result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	@Cmd(Id = ActorCmd.ADD_REPUTATION)
	public void addReputation(IoSession session, byte[] bytes, Response response) { 
		AddReputationRequest addReputationRequest = new AddReputationRequest(bytes);
		Result result = actorModifyFacade.addReputation(addReputationRequest.actorId, addReputationRequest.reputationValue);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = ActorCmd.DELETE_ACTOR_ACTIVE)
	public void deleteActorActive(IoSession session, byte[] bytes, Response response) { 
		DeleteActorActiveRequest request = new DeleteActorActiveRequest(bytes);
		Result result = actorModifyFacade.deleteActorActive(request.actorId,request.appId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = ActorCmd.CHANGE_ACTOR_UID)
	public void chnageActorUid(IoSession session, byte[] bytes, Response response) {
		ActorChangeUidRequest request = new ActorChangeUidRequest(bytes);
		Result result = actorModifyFacade.changeActorUid(request.actorId, request.newPlatformId, request.newUid, request.newChannelId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
}
