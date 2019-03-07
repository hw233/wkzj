package com.jtang.gameserver.module.equip.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

public class EquipRule {

	/**
	 * 装备最大上限数
	 */
	public static int EQUIP_MAX_NUM; // = 300;
	
	static {
		EQUIP_MAX_NUM = GlobalService.getInt("EQUIP_MAX_NUM");
	}
}
