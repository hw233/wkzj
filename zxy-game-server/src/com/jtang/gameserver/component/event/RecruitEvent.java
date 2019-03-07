package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;


/**
 * 聚仙事件
 * @author pengzy
 *
 */
public class RecruitEvent extends GameEvent{

	/**
	 * 聚仙次数
	 */
	public int num;
	
	public RecruitEvent(long actorId,int num) {
		super(EventKey.RECRUIT, actorId);
		this.num = num;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		return list;
	}
}
