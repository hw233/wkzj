package com.jtang.gameserver.module.adventures.achievement.processor;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.MoraleIncreaseEvent;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

@Component
public class MoraleAchieve extends AbstractAchieve implements Receiver {
	
	@Override
	public void register() {
		eventBus.register(EventKey.MORALE_INCREASE, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		MoraleIncreaseEvent event = (MoraleIncreaseEvent) paramEvent;
		finishNumAchievement(event.actorId, AchieveType.MORALE, event.totalMorale);
	}
}
