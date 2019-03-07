package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 聊天事件，玩家有聊天则抛出该事件
 * @author 0x737263
 *
 */
public class ChatEvent extends GameEvent {

	public ChatEvent(long actorId) {
		super(EventKey.CHAT, actorId);
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		return list;
	}
}
