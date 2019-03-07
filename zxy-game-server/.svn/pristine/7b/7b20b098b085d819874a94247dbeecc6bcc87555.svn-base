package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 切磋事件
 * @author ludd
 *
 */
public class AllyPKEvent extends GameEvent{

	private long targetActorId;
	
	public AllyPKEvent(long actorId, long targetActorId) {
		super(EventKey.ALLAY_PK, actorId);
		this.targetActorId = targetActorId;
	}
	
	public long getActorId() {
		return actorId;
	}
	
	public long getTargetActorId() {
		return targetActorId;
	}
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(targetActorId);
		return list;
	}

}
