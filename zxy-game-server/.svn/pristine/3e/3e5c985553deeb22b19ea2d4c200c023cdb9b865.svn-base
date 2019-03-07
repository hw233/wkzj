package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * VIP等级变化事件
 * @author ludd
 *
 */
public class VipLevelChangeEvent extends GameEvent {

	/**
	 * 当前等级
	 */
	public int vipLevel;

	public VipLevelChangeEvent(long actorId, int vipLevel) {
		super(EventKey.VIP_LEVEL_CHANGE, actorId);
		this.actorId = actorId;
		this.vipLevel = vipLevel;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(vipLevel);
		return list;
	}

}
