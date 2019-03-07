package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.module.equip.type.EquipAddType;

/**
 * 添加装备事件
 * @author 0x737263
 *
 */
public class AddEquipEvent extends GameEvent {

	/**
	 * 添加的装备id
	 */
	public int equipId;
		
	/**
	 * 装备添加类型
	 */
	public EquipAddType equipAddType;
	
	/**
	 * 装备配置信息
	 */
	public EquipConfig equipConfig;
	
	public AddEquipEvent(long actorId, int equipId, EquipAddType equipAddType, EquipConfig config) {
		super(EventKey.ADD_EQUIP, actorId);
		this.equipId = equipId;
		this.equipAddType = equipAddType;
		this.equipConfig = config;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(equipId);
		list.add(equipAddType);

		return list;
	}

}
