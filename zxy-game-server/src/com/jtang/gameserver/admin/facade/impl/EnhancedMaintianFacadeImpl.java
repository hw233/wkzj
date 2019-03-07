package com.jtang.gameserver.admin.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.facade.EnhancedMaintianFacade;
import com.jtang.gameserver.module.enhanced.facade.EnhancedFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class EnhancedMaintianFacadeImpl implements EnhancedMaintianFacade {

	@Autowired
	EnhancedFacade enhancedFacade;

//	@Autowired
//	EnhancedDao enhancedDao;

	@Autowired
	ActorFacade actorFacade;

	@Override
	public Result modifyEnhanced(long actorId, int targetLevel) {
//		Enhanced enhanced = enhancedFacade.get(actorId);
//		if (enhanced == null) {
//			return Result.valueOf(ENHANCED_UP_ERROR);
//		}
//		
//		int oldLevel = enhanced.level;
//		if (targetLevel > EnhancedService.getMaxLevel()) {
//			return Result.valueOf(ENHANCED_UP_LEVEL_MAX);
//		}
//		
//		if (!enhancedDao.update(actorId, targetLevel)) {
//			return Result.valueOf(ENHANCED_UP_ERROR);
//		}
//		
//		Actor actor = actorFacade.getActor(actorId);
//		GameOssLogger.enhancedUpgrade(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, oldLevel, targetLevel);
//		
		return Result.valueOf();
	}

}
