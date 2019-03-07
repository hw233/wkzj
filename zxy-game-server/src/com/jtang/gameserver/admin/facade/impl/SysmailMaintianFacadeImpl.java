package com.jtang.gameserver.admin.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.model.RewardObject;
import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.GameAdminStatusCodeConstant;
import com.jtang.gameserver.admin.facade.SysmailMaintianFacade;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.sysmail.facade.SysmailFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class SysmailMaintianFacadeImpl implements SysmailMaintianFacade {

	@Autowired
	ActorFacade actorFacade;

	@Autowired
	SysmailFacade sysmailFacade;

	@Override
	public Result sendSysMail(long actorId, List<RewardObject> list, String text) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(GameAdminStatusCodeConstant.ACTOR_NOT_EXIST);
		}
		return sysmailFacade.channelSendMail(actorId, list, text);
	}

}
