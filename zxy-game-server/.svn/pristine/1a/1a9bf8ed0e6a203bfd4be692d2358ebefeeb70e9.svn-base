package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 阵形变化事件
 * @author ludd
 *
 */
public class LineupChangeEvent extends GameEvent {

	public LineupChangeEvent(long actorId) {
		super(EventKey.LINE_UP_CHANGE, actorId);
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		return list;
	}

}
