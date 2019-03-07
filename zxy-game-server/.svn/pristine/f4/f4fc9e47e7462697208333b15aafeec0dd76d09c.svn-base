package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 登天塔次数事件
 * @author 0x737263
 *
 */
public class BableTimesEvent extends GameEvent {

	/**
	 * 登天塔次数
	 */
	public int times;
	
	public BableTimesEvent(long actorId, int times) {
		super(EventKey.BABLE_BATTLE_TIMES, actorId);
		this.times = times;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(times);
		return list;
	}

}
