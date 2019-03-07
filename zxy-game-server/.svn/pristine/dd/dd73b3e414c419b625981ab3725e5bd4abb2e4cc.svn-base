package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;


public class LineupUnlockEvent extends GameEvent {
	/**
	 * 已经解锁的格子数
	 */
	public int unlockCount;
	
	public LineupUnlockEvent(long actorId, int unlockCount) {
		super(EventKey.LINEUP_UNLOCK, actorId);
		this.unlockCount = unlockCount;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(unlockCount);
		return list;
	}
}
