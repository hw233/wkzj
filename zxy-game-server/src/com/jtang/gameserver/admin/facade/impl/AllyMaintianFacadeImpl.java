package com.jtang.gameserver.admin.facade.impl;

import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ACTOR_NOT_EXIST;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ALLY_NOT_EXIST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.facade.AllyMaintianFacade;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.ally.facade.AllyFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class AllyMaintianFacadeImpl implements AllyMaintianFacade {

	@Autowired
	ActorFacade actorFacade;

	@Autowired
	AllyFacade allyFacade;

	@Override
	public Result deleteAlly(long actorId, long allyId) {
		Actor actor = actorFacade.getActor(actorId);
		Actor allyActor = actorFacade.getActor(allyId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		if (allyActor == null) {
			return Result.valueOf(ALLY_NOT_EXIST);
		}
		Result result = allyFacade.removeAlly(actorId, allyId);
		return result;
	}

	@Override
	public Result addAlly(long actorId, long allyId) {
		Actor actor = actorFacade.getActor(actorId);
		Actor allyActor = actorFacade.getActor(allyId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		if (allyActor == null) {
			return Result.valueOf(ALLY_NOT_EXIST);
		}
		Result result = allyFacade.addAlly(actorId, allyId);
		return result;
	}

}
