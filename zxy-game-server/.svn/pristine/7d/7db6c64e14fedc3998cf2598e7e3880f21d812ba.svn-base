package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;


/**
 * 活力上限增加事件
 * @author pengzy
 *
 */
public class VitLimitEvent extends GameEvent {
	
	//当前最大活力
	public int maxVit;
	
	public VitLimitEvent(long actorId, int maxVit) {
		super(EventKey.VIT_LIMIT, actorId);
		this.maxVit = maxVit;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(maxVit);
		return list;
	}
}