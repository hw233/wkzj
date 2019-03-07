package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;


/**
 * 气势增长事件
 * @author pengzy
 *
 */
public class MoraleIncreaseEvent extends GameEvent {
	
	//当前总气势
	public int totalMorale;
	
	public MoraleIncreaseEvent(long actorId, int totalMorale) {
		super(EventKey.MORALE_INCREASE, actorId);
		this.totalMorale = totalMorale;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(totalMorale);
		return list;
	}
}