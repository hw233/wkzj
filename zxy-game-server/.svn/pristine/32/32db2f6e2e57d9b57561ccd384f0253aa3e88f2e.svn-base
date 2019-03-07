package com.jtang.gameserver.admin.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.facade.DelveMaintianFacade;
import com.jtang.gameserver.module.delve.dao.DelveDao;
import com.jtang.gameserver.module.delve.facade.DelveFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class DelveMaintianFacadeImpl implements DelveMaintianFacade {

	@Autowired
	DelveFacade delveFacade;

	@Autowired
	DelveDao delveDao;

	@Autowired
	ActorFacade actorFacade;

	@Override
	public Result modifyDelveLevel(long actorId, int level) {
//		Actor actor = actorFacade.getActor(actorId);
//		if (actor == null) {
//			return Result.valueOf(ACTOR_NOT_EXIST);
//		}
//		Delve delve = delveDao.get(actorId);
//		if (delve == null) {
//			return Result.valueOf(DELVE_LEVEL_UP_FAIL);
//		}
//
//		int oldLevel = delve.level;
//		int nextLevel = level;
//
//		if (nextLevel > DelveService.delveMaxLevel()) {
//			return Result.valueOf(DELVE_LEVEL_UP_IS_MAX);
//		}
//
//		boolean result = delveDao.update(actorId, nextLevel);
//		if (result == false) {
//			return Result.valueOf(DELVE_LEVEL_UP_FAIL);
//		}
//
//		DelvePushHelper.pushAttribute(actorId, DelveAttributeKey.LEVEL, nextLevel);
//
//		// oss
//		GameOssLogger.delveUpgrade(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, oldLevel, nextLevel);

		return Result.valueOf();
	}

}
