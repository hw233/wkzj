package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 删除盟友事件
 * @author pengzy
 *
 */
public class RemoveAllyEvent extends GameEvent {
	public long allyActorId;

	public RemoveAllyEvent(long actorId, long allyActorId) {
		super(EventKey.REMOVE_ALLY, actorId);
		this.allyActorId = allyActorId;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(allyActorId);
		return list;
	}
}
