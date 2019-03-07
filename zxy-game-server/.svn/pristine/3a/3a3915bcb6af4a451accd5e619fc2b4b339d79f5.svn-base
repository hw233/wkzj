package com.jtang.gameserver.module.adventures.achievement.processor;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.SnatchResultEvent;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

@Component
public class SnatchAchieve extends AbstractAchieve implements Receiver {

	@Override
	public void register() {
		eventBus.register(EventKey.SNATCH_RESULT, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		SnatchResultEvent event = (SnatchResultEvent) paramEvent;
		if (event.isGold()) {
			finishSumAccumulateAchieve(event.actorId, AchieveType.SNATCH_GOLD, event.goodsNum);
		}
		finishNumAchievement(event.actorId, AchieveType.SNATCH_NUM);
		finishSumAccumulateAchieve(event.actorId, AchieveType.SNATCH_SCORE, event.ownScore);
	}
	
}