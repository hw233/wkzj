package com.jtang.gameserver.admin.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.db.DBQueue;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.admin.GameAdminModule;
import com.jtang.gameserver.admin.facade.MaintainFacade;
import com.jtang.gameserver.admin.handler.request.ActorEntityRequest;
import com.jtang.gameserver.admin.handler.request.AddUidRequest;
import com.jtang.gameserver.admin.handler.request.ChangeBlockRequest;
import com.jtang.gameserver.admin.handler.request.ClearActorEntityCacheRequest;
import com.jtang.gameserver.admin.handler.request.FlushDataConfigRequest;
import com.jtang.gameserver.admin.handler.request.KickPlayerRequest;
import com.jtang.gameserver.admin.handler.request.OpenCloseServerRequest;
import com.jtang.gameserver.admin.handler.request.SystemNoticeRequest;
import com.jtang.gameserver.admin.handler.response.ActorEntityResponse;
import com.jtang.gameserver.admin.handler.response.DBEntityQueueInfoResponse;
import com.jtang.gameserver.admin.handler.response.OnlinePlayerNumResponse;
import com.jtang.gameserver.admin.handler.response.ServerStateResponse;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;

/**
 * 服务器维护接口
 * @author 0x737263
 *
 */
@Component
public class MaintainHandler extends AdminRouterHandlerImpl {

	@Autowired
	private MaintainFacade maintainFacade;
	
	@Autowired
	private DBQueue dbQueue;
	
	@Override
	public byte getModule() {
		return GameAdminModule.MAINTAIN;
	}
	
	@Cmd(Id = MaintainCmd.CHANGE_SERVER_STATE)
	public void openCloseServer(IoSession session, byte[] bytes, Response response){
		OpenCloseServerRequest openCloseServerRequest = new OpenCloseServerRequest(bytes);
		Result result = maintainFacade.changeServerState(openCloseServerRequest.flag, openCloseServerRequest.time);
		if (result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = MaintainCmd.SEND_SYSTEM_NOTICE)
	public void systemNotice(IoSession session, byte[] bytes, Response response){
		SystemNoticeRequest systemNoticeRequest = new SystemNoticeRequest(bytes);
		Result result = maintainFacade.sendNotice(systemNoticeRequest.message, systemNoticeRequest.pollingNum, systemNoticeRequest.delayTime,
				systemNoticeRequest.channelIds);
		if (result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = MaintainCmd.ONLINE_NUM)
	public void getOnlinePlayerNum(IoSession session, byte[] bytes, Response response){
		int historyMinNum =  maintainFacade.getHistorMinPlayerNum();
		int historyMaxNum =  maintainFacade.getHistorMaxPlayerNum();
		int online =  maintainFacade.getOnlinePlayerNum();
		
		OnlinePlayerNumResponse onlinePlayerNumResponse = new OnlinePlayerNumResponse(historyMinNum, historyMaxNum, online);
		sessionWrite(session, response, onlinePlayerNumResponse);
	}
	
	@Cmd(Id = MaintainCmd.SHUTDOWN_SERVER)
	public void closeServer(IoSession session, byte[] bytes, Response response) {
		maintainFacade.shutdownServer();
		sessionWrite(session, response);
	}
	@Cmd(Id = MaintainCmd.HEARTBEAT)
	public void heartBeat(IoSession session, byte[] bytes, Response response) {
		sessionWrite(session, response);
	}
	@Cmd(Id = MaintainCmd.KICK_OF_PLAYER)
	public void kickOffPlayer(IoSession session, byte[] bytes, Response response) {
		KickPlayerRequest kickPlayerRequest = new KickPlayerRequest(bytes);
		Result result = maintainFacade.kickPlayr(kickPlayerRequest.actorId);
		if (result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	@Cmd(Id = MaintainCmd.GET_SERVER_STATE)
	public void getServerState(IoSession session, byte[] bytes, Response response) {
		ServerStateResponse serverStateResponse = new ServerStateResponse();
		sessionWrite(session, response, serverStateResponse);
	}
	
	@Cmd(Id = MaintainCmd.DB_ENTITY_INFO)
	public void dbEntityQueueInfo(IoSession session, byte[] bytes, Response response){
		DBEntityQueueInfoResponse dbEntityQueueInfoResponse = new DBEntityQueueInfoResponse(dbQueue.getTaskSize(), dbQueue.getNormalEntitySize(), dbQueue.getActorSize());
		sessionWrite(session, response, dbEntityQueueInfoResponse);
	}
	@Cmd(Id = MaintainCmd.SUBMIT_DB_ENTITY)
	public void dbEntitySubmit(IoSession session, byte[] bytes, Response response){
		ChangeBlockRequest changeBlockRequest = new ChangeBlockRequest(bytes);
		dbQueue.changeBlockTime(changeBlockRequest.flag);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = MaintainCmd.FLUSH_DATA_CONFIG)
	public void FlushDataConfig(IoSession session, byte[] bytes, Response response){
		FlushDataConfigRequest flushDataConfig = new FlushDataConfigRequest(bytes);
		Result result = maintainFacade.flushDataConfig(flushDataConfig.fileName,flushDataConfig.data);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = MaintainCmd.GET_ACTOR_ENTITY)
	public void getActorEntity(IoSession session, byte[] bytes, Response response) {
		ActorEntityRequest actorEntityRequest = new ActorEntityRequest(bytes);
		TResult<String> result = maintainFacade.entity2JSON(actorEntityRequest.tableName, actorEntityRequest.actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			ActorEntityResponse actorEntityResponse = new ActorEntityResponse(result.item);
			sessionWrite(session, response, actorEntityResponse);
		}
	}
	
	@Cmd(Id = MaintainCmd.ADD_UID)
	public void addUid(IoSession session, byte[] bytes, Response response){
		AddUidRequest request = new AddUidRequest(bytes);
		Result result = maintainFacade.addUid(request.uid);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = MaintainCmd.CLEAN_UID)
	public void cleanUid(IoSession session, byte[] bytes, Response response){
		Result result = maintainFacade.cleanUid();
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = MaintainCmd.CLEAR_DB_INFO)
	public void clearDBEntityCache(IoSession session, byte[] bytes, Response response) {
		ClearActorEntityCacheRequest clearActorEntityCacheRequest = new ClearActorEntityCacheRequest(bytes);
		Result result = maintainFacade.clearDBEntityCache(clearActorEntityCacheRequest.actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	
}
