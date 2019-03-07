package com.jtang.gameserver.module.hole.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

public class HoleRule {

	/*
	 * 洞府领取大礼包需要通关的盟友数量
	 */
	public static int HOLE_ALLY_COUNT;

	/**
	 * 主动触发洞府的次数(公式)
	 */
	public static int HOLE_TRIGGER_SELF_COUNT;
	
	/**
	 * 盟友邀请触发洞府的次数(公式)
	 */
	public static int HOLE_TRIGGER_ALLY_COUNT;
	
	/**
	 * 开放等级
	 */
	public static int HOLE_OPEN_LEVEL;

	static {
		HOLE_ALLY_COUNT = GlobalService.getInt("HOLE_ALLY_COUNT");
		HOLE_TRIGGER_SELF_COUNT = GlobalService.getInt("HOLE_TRIGGER_SELF_COUNT");
		HOLE_TRIGGER_ALLY_COUNT = GlobalService.getInt("HOLE_TRIGGER_ALLY_COUNT");
		HOLE_OPEN_LEVEL = GlobalService.getInt("HOLE_OPEN_LEVEL");
	}

}
