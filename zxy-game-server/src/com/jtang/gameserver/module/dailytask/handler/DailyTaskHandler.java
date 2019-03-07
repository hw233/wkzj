package com.jtang.gameserver.module.dailytask.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.gameserver.module.dailytask.facade.DailyTaskFacade;
import com.jtang.gameserver.module.dailytask.handler.request.GetDailyTaskRewardRequest;
import com.jtang.gameserver.module.dailytask.handler.response.DailyTaskInfoResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
@Component
public class DailyTaskHandler extends GatewayRouterHandlerImpl {

	@Autowired
	private DailyTaskFacade dailyTaskFacade;
	@Override
	public byte getModule() {
		return ModuleName.DAILY_TASK;
	}
	@Cmd(Id = DailyTaskCmd.GET_DAILY_TASK_INFO)
	public void getDailyTaskInfo(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		DailyTaskInfoResponse result = dailyTaskFacade.getDailyTask(actorId);

		sessionWrite(session, response, result);
	}
	@Cmd(Id = DailyTaskCmd.GET_REWARD)
	public void getReward(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		GetDailyTaskRewardRequest getDailyTaskRewardRequest = new GetDailyTaskRewardRequest(bytes);
		short result = dailyTaskFacade.getReward(actorId, getDailyTaskRewardRequest.taskId);
		response.setStatusCode(result);
		sessionWrite(session, response);
	}
}
