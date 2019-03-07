package com.jtang.gameserver.admin.facade.impl;

import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ACTOR_NOT_EXIST;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.TARGET_ACTOR_NOT_EXIST;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.admin.facade.MsgMaintianFacade;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Message;
import com.jtang.gameserver.module.msg.facade.MsgFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class MsgMaintianFacadeImpl implements MsgMaintianFacade {

	@Autowired
	MsgFacade msgFacade;

	@Autowired
	ActorFacade actorFacade;

	@Override
	public Result deleteMsg(long actorId, long msgId) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		List<Long> mIdList = new ArrayList<>();
		mIdList.add(msgId);
		return msgFacade.removeMsg(actorId, mIdList);
	}

	@Override
	public Result sendMsg(long actorId, long toActorId, String content) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		Actor toActor = actorFacade.getActor(toActorId);
		if (toActor == null) {
			return Result.valueOf(TARGET_ACTOR_NOT_EXIST);
		}
		TResult<Message> msg = msgFacade.sendMsg(actorId, toActorId, content);
		return Result.valueOf(msg.statusCode);
	}

}
