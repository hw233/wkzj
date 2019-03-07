package com.jtang.gameserver.module.adventures.achievement.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.ActorLevelUpEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.PowerRankChangeEvent;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;
import com.jtang.gameserver.module.power.constant.PowerRule;
import com.jtang.gameserver.module.power.dao.PowerExtDao;
import com.jtang.gameserver.module.power.facade.PowerFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;

/**
 * 最强势力排名变更达成
 * @author 0x737263
 *
 */
@Component
public class PowerRankAchieve extends AbstractAchieve implements Receiver {

	@Autowired
	PowerFacade powerFacade;
	
	@Autowired
	PowerExtDao powerExtDao;
	
	@Override
	public void register() {
		eventBus.register(EventKey.POWER_RANK_CHANGE, this);
		eventBus.register(EventKey.ACTOR_LEVEL_UP, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		
		if (paramEvent.getName() == EventKey.POWER_RANK_CHANGE) {
			PowerRankChangeEvent rankChangeEvent = (PowerRankChangeEvent) paramEvent;
			finishAGreaterAndBLessNum(rankChangeEvent.actorId, AchieveType.POWER_TOP_RANK, rankChangeEvent.actorLevel, rankChangeEvent.newRank);
		}
		
		// 如果有升级事件，也检测一下是否达成最强势力排名达成
		if (paramEvent.getName() == EventKey.ACTOR_LEVEL_UP) {
			ActorLevelUpEvent actorEvent = (ActorLevelUpEvent) paramEvent;
			int level = ActorHelper.getActorLevel(actorEvent.actorId);
			if (actorEvent != null && actorEvent.actor != null && level >= PowerRule.POWER_UP_LEVEL) {
//				int rank = powerFacade.getPower(actorEvent.actorId).getRank();
				int rank = powerExtDao.getPowerExt(actorEvent.actorId).historyRank;
				finishAGreaterAndBLessNum(actorEvent.actorId, AchieveType.POWER_TOP_RANK, actorEvent.actor.level, rank);
			}
		}

	}

}
