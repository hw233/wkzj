package com.jtang.gameserver.module.adventures.achievement.processor;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.ChatEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

@Component
public class ChatNumAchieve extends AbstractAchieve implements Receiver {

	@Override
	public void register() {
		eventBus.register(EventKey.CHAT, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		ChatEvent event = (ChatEvent) paramEvent;
		finishNumAchievement(event.actorId, AchieveType.CHAT_NUM);
	}

}
