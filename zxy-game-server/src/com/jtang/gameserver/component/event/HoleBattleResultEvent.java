package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 洞府战斗结果事件
 * @author ludd
 *
 */
public class HoleBattleResultEvent extends GameEvent{

	public HoleBattleResultEvent(long actorId) {
		super(EventKey.HOLE_BATTLE_RESULT, actorId);
	}
	
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		return list;
	}

}
