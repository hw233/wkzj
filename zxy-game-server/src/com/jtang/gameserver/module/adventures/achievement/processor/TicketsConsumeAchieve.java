package com.jtang.gameserver.module.adventures.achievement.processor;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.UseTicketsEvent;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

@Component
public class TicketsConsumeAchieve extends AbstractAchieve implements Receiver {
	
	@Override
	public void register() {
		eventBus.register(EventKey.TICKETS_USE, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		UseTicketsEvent event = (UseTicketsEvent) paramEvent;
		finishNumAchievement(event.actorId, AchieveType.TICKETS_CONSUME, event.totalSum);
	}
	
}