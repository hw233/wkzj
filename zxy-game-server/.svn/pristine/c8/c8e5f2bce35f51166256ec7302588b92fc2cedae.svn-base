package com.jtang.gameserver.module.adventures.achievement.processor;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.EnergyLimitEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

@Component
public class EnergyLimitAchieve extends AbstractAchieve implements Receiver {
	
	@Override
	public void register() {
		eventBus.register(EventKey.ENERGY_LIMIT, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		EnergyLimitEvent event = (EnergyLimitEvent) paramEvent;
		finishNumAchievement(event.actorId, AchieveType.ENERGY_LIMIT, event.maxEnergy);
	}
}
