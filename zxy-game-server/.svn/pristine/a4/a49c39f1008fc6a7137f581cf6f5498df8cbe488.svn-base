package com.jtang.gameserver.module.adventures.achievement.processor;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.LineupUnlockEvent;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

@Component
public class LineupUnLockAchieve extends AbstractAchieve implements Receiver {

	@Override
	public void onEvent(Event paramEvent) {
		LineupUnlockEvent event = (LineupUnlockEvent) paramEvent;
		finishNumAchievement(event.actorId, AchieveType.LINEUP_UNLOCK, event.unlockCount);
	}

	@Override
	public void register() {
		eventBus.register(EventKey.LINEUP_UNLOCK, this);
	}

}
