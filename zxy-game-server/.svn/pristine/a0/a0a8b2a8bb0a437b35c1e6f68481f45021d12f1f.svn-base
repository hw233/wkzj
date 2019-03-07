package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;


/**
 * 装备强化事件
 * @author pengzy
 *
 */
public class EquipEnhancedEvent  extends GameEvent{
	
	
	public int equipId;
	
	public int level;
	
	public int upgradeNum;
	
	public EquipEnhancedEvent(long actorId, int equipId, int level,int upgradeNum) {
		super(EventKey.EQUIP_ENHANCED, actorId);
		this.equipId = equipId;
		this.level = level;
		this.upgradeNum = upgradeNum;
	}

	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(equipId);
		list.add(level);
		list.add(upgradeNum);
		return list;
	}
}