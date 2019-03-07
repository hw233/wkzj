package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 修改名字的事件
 * @author pengzy
 *
 */
public class NameChangeEvent extends GameEvent{

	public NameChangeEvent(long actorId) {
		super(EventKey.NAME_CHANGED, actorId);
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		return list;
	}
}