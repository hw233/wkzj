package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 使用点券事件，用于累计点券消耗总量
 * @author pengzy
 *
 */
public class UseTicketsEvent extends GameEvent{
	
	/**
	 * 自游戏开始，已经使用的点券总量
	 */
	public int totalSum;
	
	public UseTicketsEvent(long actorId, int totalSum) {
		super(EventKey.TICKETS_USE, actorId);
		this.totalSum = totalSum;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(totalSum);
		return list;
	}
}
