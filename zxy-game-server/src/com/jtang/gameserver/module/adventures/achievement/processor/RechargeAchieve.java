package com.jtang.gameserver.module.adventures.achievement.processor;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.RechargeTicketsEvent;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;


@Component
public class RechargeAchieve extends AbstractAchieve implements Receiver {

	@Override
	public void register() {
		eventBus.register(EventKey.TICKETS_RECHARGE, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		RechargeTicketsEvent event = (RechargeTicketsEvent) paramEvent;
		finishNumAchievement(event.actorId, AchieveType.TICKETS_RECHARGE, event.totalSum);
	}
}
