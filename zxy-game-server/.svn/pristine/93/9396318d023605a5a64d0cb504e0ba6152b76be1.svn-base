package com.jtang.gameserver.module.app.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ListResult;
import com.jtang.core.result.Result;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.module.app.facade.AppFacade;
import com.jtang.gameserver.module.app.handler.request.GetRewardRequest;
import com.jtang.gameserver.module.app.handler.response.GetAppGlobalResponse;
import com.jtang.gameserver.module.app.handler.response.GetAppRecordResponse;
import com.jtang.gameserver.module.app.handler.response.GetRewardResponse;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class AppHanlder extends GatewayRouterHandlerImpl {

	@Override
	public byte getModule() {
		return ModuleName.APP;
	}
	
	@Autowired
	AppFacade appFacade;
	
	/**
	 * 获取活动配置
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = AppCmd.GET_APP_GLOBAL)
	public void getAppConfig(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		//GetAppGlobalRequest request = new GetAppGlobalRequest(bytes);
		List<AppGlobalVO> cfgs = new ArrayList<>();
		for (long id : AppRuleService.getAllApp()) {
			Result result = appFacade.appEnable(actorId,id);
			if (result.isFail()) {
				continue;
			}
			AppGlobalVO appGlobalVO = appFacade.getAppGlobalVO(actorId,id);
			if (appGlobalVO != null) {
				cfgs.add(appGlobalVO);
			}
		}

		GetAppGlobalResponse packet = new GetAppGlobalResponse(cfgs);
		sessionWrite(session, response, packet);
	}
	
	/**
	 * 获取活动记录
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = AppCmd.PUSH_APP_RECORD)
	public void getAppRecord(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		//GetAppRecordRequest request = new GetAppRecordRequest(bytes);
		
		List<AppRecordVO> list = new ArrayList<>();
		for (long id : AppRuleService.getAllApp()) {
			Result result = appFacade.appEnable(actorId,id);
			if (result.isFail()) {
				continue;
			}
			AppRecordVO appRecordVO = appFacade.getAppRecord(actorId,id);
			if (appRecordVO != null) {
				list.add(appRecordVO);
			}
		}

		GetAppRecordResponse packet = new GetAppRecordResponse(list);
		sessionWrite(session, response, packet);
	}
	
	/**
	 * 领取活动奖励
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = AppCmd.GET_REWARD)
	public void reward(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		GetRewardRequest request = new GetRewardRequest(bytes);
		Result r = appFacade.appEnable(actorId,request.appId);
		if (r.isFail()) {
			response.setStatusCode(r.statusCode);
			sessionWrite(session, response);
			return;
		}
		
		ListResult<RewardObject> result = appFacade.getReward(actorId,request.appId, request.paramsMaps);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
			return;
		}

		GetRewardResponse packet = new GetRewardResponse(result.item);
		sessionWrite(session, response, packet);
	}
	

}
