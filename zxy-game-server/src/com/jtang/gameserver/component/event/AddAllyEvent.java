package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 添加盟友事件
 * @author pengzy
 *
 */
public class AddAllyEvent extends GameEvent {
	
	/**
	 * 当前角色自己的盟友总数
	 */
	public int mineTotalNum;
	
	/**
	 * 被添加的盟友Id
	 */
	public long allyActorId;
	/**
	 * 被添加的盟友总数
	 */
	public int allyTotalNum;

	public AddAllyEvent(long actorId, int mineTotalNum, long allyActorId, int allyTotalNum) {
		super(EventKey.ADD_ALLY, actorId);
		this.actorId = actorId;
		this.mineTotalNum = mineTotalNum;

		this.allyActorId = allyActorId;
		this.allyTotalNum = allyTotalNum;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(mineTotalNum);
		list.add(allyActorId);
		list.add(allyTotalNum);
		return list;
	}
}
