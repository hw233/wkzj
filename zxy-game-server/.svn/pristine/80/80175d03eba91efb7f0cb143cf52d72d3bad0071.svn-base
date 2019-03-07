package com.jtang.gameserver.module.adventures.achievement.processor;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.ActorLevelUpEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

@Component
public class ActorLevelAchieve extends AbstractAchieve implements Receiver {

	@Override
	public void register() {
		eventBus.register(EventKey.ACTOR_LEVEL_UP, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		ActorLevelUpEvent event = (ActorLevelUpEvent) paramEvent;
		long actorId = event.actor.getPkId();
		int actorLevel = event.actor.level;

		finishNumAchievement(actorId, AchieveType.ACTOR_LEVEL, actorLevel);
	}
}
