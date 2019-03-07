package com.jtang.gameserver.module.adventures.achievement.processor;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.AddAllyEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

@Component
public class AllySumAchieve extends AbstractAchieve implements Receiver {
	
	@Override
	public void register() {
		eventBus.register(EventKey.ADD_ALLY, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		AddAllyEvent event = (AddAllyEvent) paramEvent;
		finishNumAchievement(event.actorId, AchieveType.ALLY_NUM, event.mineTotalNum);
		finishNumAchievement(event.allyActorId, AchieveType.ALLY_NUM, event.allyTotalNum);
	}
}
