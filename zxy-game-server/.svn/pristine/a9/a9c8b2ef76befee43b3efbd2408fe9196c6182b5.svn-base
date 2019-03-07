package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 送礼事件
 * @author ludd
 *
 */
public class AllyGiveGiftEvent extends GameEvent{

	private long targetActorId;
	
	public AllyGiveGiftEvent(long actorId, long targetActorId) {
		super(EventKey.GIVE_GIFT, actorId);
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
