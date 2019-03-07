package com.jtang.gameserver.module.adventures.achievement.processor;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.PowerBattleEvent;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

/**
 * 排行榜战斗次数成就
 * @author jianglf
 *
 */
@Component
public class PowerFightAchieve extends AbstractAchieve implements Receiver {

	@Override
	public void onEvent(Event paramEvent) {
		PowerBattleEvent event = paramEvent.convert();
		finishNumAchievement(event.actorId, AchieveType.POWER_FIGHT);
	}

	@Override
	public void register() {
		eventBus.register(EventKey.POWER_BATTLE_RESULT, this);
	}

}
