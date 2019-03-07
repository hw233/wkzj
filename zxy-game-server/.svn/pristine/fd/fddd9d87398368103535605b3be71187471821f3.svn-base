package com.jtang.gameserver.admin.facade.impl;

//import static com.jiatang.common.GameStatusCodeConstant.REACH_LEVEL_CAP;
import static com.jtang.core.protocol.StatusCode.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.facade.ReciruitMaintianFacade;
import com.jtang.gameserver.module.recruit.dao.RecruitDao;
import com.jtang.gameserver.module.recruit.facade.RecruitFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class ReciruitMaintianFacadeImpl implements ReciruitMaintianFacade {

	@Autowired
	ActorFacade actorFacade;

	@Autowired
	RecruitFacade recruitFacade;

	@Autowired
	RecruitDao recruitDao;

	@Override
	public Result modifyReciruitLevel(long actorId, int level) {
//		Actor actor = actorFacade.getActor(actorId);
//		if (actor == null) {
//			return Result.valueOf(ACTOR_NOT_EXIST);
//		}
//		Recruit recruit = recruitFacade.getInfo(actorId);
//
//		int oldLevel = recruit.level;
//
//		if (oldLevel == level) {
//			return Result.valueOf();
//		}
//
//		if (level > RecruitService.maxLevel()) {
//			return Result.valueOf(REACH_LEVEL_CAP);
//		}
//
//		int upgradeAddChange = recruitDao.get(actorId).todayTotalChange;
//		if (oldLevel > level) {
//			for (int i = level; i <= oldLevel; i++) {
//				upgradeAddChange -= RecruitService.get(i).getUpgradeAddChange();
//			}
//		} else {
//			for (int i = oldLevel; i <= level; i++) {
//				upgradeAddChange += RecruitService.get(i).getUpgradeAddChange();
//			}
//		}
//		recruit.todayTotalChange = upgradeAddChange;
//		recruitDao.update(recruit);
//		GameOssLogger.recruitUpgrade(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, oldLevel, level);
//		RecruitPushHelper.push(actorId);
		return Result.valueOf(SUCCESS);
	}

}
