package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

public class ActorBuyGoldEvent extends GameEvent {
	
	/**
	 * 购买次数
	 */
	public int buyNum;

	public ActorBuyGoldEvent(long actorId,int buyNum) {
		super(EventKey.ACTOR_BUY_GOLD, actorId);
		this.buyNum = buyNum;
	}

	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(buyNum);
		return list;
	}

}
