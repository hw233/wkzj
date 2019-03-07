package com.jtang.gameserver.module.adventures.achievement.processor;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.TrialBattleResultEvent;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;


@Component
public class TrialCaveAchieve extends AbstractAchieve implements Receiver {

	@Override
	public void register() {
		eventBus.register(EventKey.TRIAL_BATTLE_RESULT, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		TrialBattleResultEvent event = (TrialBattleResultEvent) paramEvent;
		finishNumAchievement(event.actorId, AchieveType.TRIALCAVE);
	}
}
