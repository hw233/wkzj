package com.jtang.gameserver.module.dailytask.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.dailytask.handler.DailyTaskCmd;
import com.jtang.gameserver.module.dailytask.model.DailyTaskVO;
import com.jtang.gameserver.server.broadcast.Broadcast;
@Component
public class DailyTaskPushHelper {
	@Autowired
	Broadcast broadcast;
	
	private static ObjectReference<DailyTaskPushHelper> ref = new ObjectReference<DailyTaskPushHelper>();
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	public static void pushTask(long actorId, DailyTaskVO dailyTaskVO) {
		Response response = Response.valueOf(ModuleName.DAILY_TASK, DailyTaskCmd.PUSH_DAILY_TASK_PROGRESS, dailyTaskVO.getBytes());
		ref.get().broadcast.push(actorId, response);
	}

	public static void pushReset(long actorId) {
		Response response = Response.valueOf(ModuleName.DAILY_TASK, DailyTaskCmd.PUSH_DAILY_TASK_RESET);
		ref.get().broadcast.push(actorId, response);
	}
}
