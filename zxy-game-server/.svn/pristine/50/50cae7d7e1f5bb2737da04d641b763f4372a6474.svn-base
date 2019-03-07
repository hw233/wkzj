package com.jtang.gameserver.module.adventures.achievement.processor;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.VitLimitEvent;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

@Component
public class VitAchieve extends AbstractAchieve implements Receiver {

	@Override
	public void register() {
		eventBus.register(EventKey.VIT_LIMIT, this);
	}

	@Override
	public void onEvent(Event paramEvent) {
		VitLimitEvent event = (VitLimitEvent) paramEvent;
		finishNumAchievement(event.actorId, AchieveType.VIT_LIMIT, event.maxVit);
	}
}

