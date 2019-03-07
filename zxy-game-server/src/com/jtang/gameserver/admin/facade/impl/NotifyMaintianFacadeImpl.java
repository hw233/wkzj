package com.jtang.gameserver.admin.facade.impl;

import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ACTOR_NOT_EXIST;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.NOTIFY_ID_ERROR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.facade.NotifyMaintianFacade;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.notify.facade.NotifyFacade;
import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class NotifyMaintianFacadeImpl implements NotifyMaintianFacade {

	@Autowired
	NotifyFacade notifyFacade;
	
	@Autowired
	ActorFacade actorFacade;
	
	@Override
	public Result deleteNotify(long actorId, long notifyId) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}

		AbstractNotifyVO notifyVO = notifyFacade.get(actorId, notifyId);
		if (notifyVO == null) {
			return Result.valueOf(NOTIFY_ID_ERROR);
		}
		return notifyFacade.remove(actorId, notifyId);
	}

}
