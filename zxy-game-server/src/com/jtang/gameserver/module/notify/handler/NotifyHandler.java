package com.jtang.gameserver.module.notify.handler;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.FightVideo;
import com.jtang.gameserver.module.notify.facade.NotifyFacade;
import com.jtang.gameserver.module.notify.handler.request.GetRewardRequest;
import com.jtang.gameserver.module.notify.handler.request.NotifyAllyRequest;
import com.jtang.gameserver.module.notify.handler.request.RemoveNotifyRequest;
import com.jtang.gameserver.module.notify.handler.request.ReplayFightVideoRequest;
import com.jtang.gameserver.module.notify.handler.response.NotifyListResponse;
import com.jtang.gameserver.module.notify.handler.response.ReplayFightVideoResponse;
import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class NotifyHandler extends GatewayRouterHandlerImpl{

	@Autowired
	private NotifyFacade notifyFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.NOTIFY;
	}
	
	@Cmd(Id = NotifyCmd.GET_NOTIFY)
	public void getNotify(IoSession session, byte[] bytes, Response response) {
		long toActorId = playerSession.getActorId(session);

		List<AbstractNotifyVO> list = notifyFacade.getList(toActorId);
		NotifyListResponse packet = new NotifyListResponse(list);
		sessionWrite(session, response, packet);
	}
	
	@Cmd(Id = NotifyCmd.GET_REWARD)
	public void getReward(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		GetRewardRequest request = new GetRewardRequest(bytes);
		Result result = notifyFacade.getReward(actorId, request.nId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = NotifyCmd.NOTICE_ALLY)
	public void noticeAlly(IoSession session, byte[] bytes, Response response) {
		long toActorId = playerSession.getActorId(session);
		NotifyAllyRequest request = new NotifyAllyRequest(bytes);
		Result result = notifyFacade.notifyAlly(toActorId, request.nId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = NotifyCmd.REMOVE_NOTIFY)
	public void removeNotify(IoSession session, byte[] bytes, Response response) {
		long toActorId = playerSession.getActorId(session);
		RemoveNotifyRequest request = new RemoveNotifyRequest(bytes);
		Result result = notifyFacade.remove(toActorId, request.nIds);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = NotifyCmd.SET_READED)
	public void setReaded(IoSession session, byte[] bytes, Response response) {
		long toActorId = playerSession.getActorId(session);
		RemoveNotifyRequest request = new RemoveNotifyRequest(bytes);
		Result result = notifyFacade.setReaded(toActorId,request.nIds);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = NotifyCmd.REPLAY_FIGHT_VIDEO)
	public void replayFightVideo(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		ReplayFightVideoRequest request = new ReplayFightVideoRequest(bytes);
		TResult<FightVideo> result = notifyFacade.getFightVideo(actorId, request.notifyId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
			return;
		}

		ReplayFightVideoResponse packet = new ReplayFightVideoResponse(result.item.videoData);
		sessionWrite(session, response, packet);
	}
}
