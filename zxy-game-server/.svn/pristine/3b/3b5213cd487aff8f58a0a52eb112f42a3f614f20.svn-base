package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 连续登陆事件，当连续登陆次数有变更的时候抛出
 * @author 0x737263
 *
 */
public class ContinueLoginEvent extends GameEvent {

	/**
	 * 连续登陆的天数
	 */
	public int day;
	
	public ContinueLoginEvent(long actorId, int day) {
		super(EventKey.CONTINUE_LOGIN, actorId);	
		this.day = day;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(day);
		return list;
	}
}
