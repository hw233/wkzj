package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 精炼装备事件
 * @author pengzy
 *
 */
public class EquipRefinedEvent extends GameEvent{

	public int equipId;
	
	public int refineNum;
	
	public EquipRefinedEvent(long actorId, int equipId, int refineNum) {
		super(EventKey.EQUIP_REFINED, actorId);
		this.equipId = equipId;
		this.refineNum = refineNum;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(equipId);
		list.add(refineNum);
		return list;
	}

}