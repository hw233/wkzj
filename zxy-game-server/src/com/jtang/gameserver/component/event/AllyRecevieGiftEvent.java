package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 收礼事件
 * @author ludd
 *
 */
public class AllyRecevieGiftEvent extends GameEvent{

	private long targetActorId;
	
	public AllyRecevieGiftEvent(long actorId, long targetActorId) {
		super(EventKey.RECEIVE_GIFT, actorId);
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
