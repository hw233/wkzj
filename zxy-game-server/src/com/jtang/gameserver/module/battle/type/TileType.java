package com.jtang.gameserver.module.battle.type;

/**
 * 格子占位类型
 * @author 0x737263
 *
 */
public interface TileType {

	/**
	 * 可走路点
	 */
	int ROAD_POINT = 0;
	
	/**
	 * 障碍占位路点
	 */
	int BARRIER_POINT = 1;
}
