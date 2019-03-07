package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;


/**
 * 排行榜挑战事件事件
 * @author ludd
 *
 */
public class PowerBattleEvent extends GameEvent {
	

	public PowerBattleEvent(long actorId) {
		super(EventKey.POWER_BATTLE_RESULT, actorId);
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		return list;
	}
}
