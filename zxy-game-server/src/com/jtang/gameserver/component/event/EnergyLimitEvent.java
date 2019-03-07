package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 精力上限增加事件
 * @author pengzy
 *
 */
public class EnergyLimitEvent extends GameEvent {

	/**
	 * 当前最大精力
	 */
	public int maxEnergy;

	public EnergyLimitEvent(long actorId, int maxEnergy) {
		super(EventKey.ENERGY_LIMIT, actorId);
		this.actorId = actorId;
		this.maxEnergy = maxEnergy;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(maxEnergy);
		return list;
	}
}