package com.jtang.gameserver.admin.facade.impl;

import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ACTOR_NOT_EXIST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.facade.AchieveMaintianFacade;
import com.jtang.gameserver.dataconfig.model.AchievementConfig;
import com.jtang.gameserver.dataconfig.service.AchievementService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.adventures.achievement.dao.AchieveDao;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class AchieveMaintianFacadeImpl implements AchieveMaintianFacade {

	@Autowired
	ActorFacade actorFacade;
	
	@Autowired
	AchieveDao achieveDao;

	/*@Override
	public Result addAchieve(long actorId, int achieveId) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		AchievementConfig achievementConfig = AchievementService.get(achieveId);
		AchieveType achieveType= AchieveType.getType(achievementConfig.getAchieveType());
		switch(achieveType){
		case ACTOR_LEVEL:
			break;
		case BABLE_FLOOR:
			break;
		case TICKETS_CONSUME:
			break;
		case TICKETS_RECHARGE:
			break;
		case WEAPON:
			break;
		case ARMOR:
			break;
		case DECORATION:
			break;
		case HERO_SUM:
			break;
		case EQUIP_REFINE:
			break;
		case STORY_PASSED:
			break;
		case MORALE:
			break;
		case EQUIP_ENHANCED_LEVEL:
			break;
		case HERO_LEVEL:
			break;
		case ENERGY_LIMIT:
			break;
		case VIT_LIMIT:
			break;
		case SNATCH_GOLD:
			break;
		case NAME_CHANGE:
			break;
		case SNATCH_NUM:
			break;
		case ALLY_NUM:
			break;
		case REFINE_NUM:
			break;
		case SNATCH_SCORE:
			break;
		case CHAT_NUM:
			break;
		case SHOP_BUY:
			break;
		}
		return null;
	}
*/
	@Override
	public Result deleteAchieve(long actorId, int achieveId) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		AchievementConfig achievementConfig = AchievementService.get(achieveId);
		achieveDao.deleteAchieve(actorId,achieveId,achievementConfig.getAchieveType());
		return Result.valueOf();
	}

}
