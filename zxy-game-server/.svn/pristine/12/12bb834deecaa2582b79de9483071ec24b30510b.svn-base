package com.jtang.gameserver.module.battle.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

/**
 * 战斗模块常量
 * @author vinceruan
 *
 */
public class BattleRule {
	/**
	 * 战场上两支队伍之间的默认间距
	 */
	public static int BATTLE_BETWEEN_GRID_NUM; // = 2;
	
	/**
	 * 敌方朝向(朝下)
	 */
	public static byte BATTLE_ENEMY_TEAM_FACE; // = 1;
	
	/**
	 * 我方朝向(朝上)
	 */
	public static byte BATTLE_MY_TEAM_FACE; // = 2;
	
	
	/**
	 * 防御系数
	 */
	public static int BATTLE_DEF_FACTOR; // = 4;
	
	/**
	 * 每场战斗的最大耗时,单位:毫秒
	 */
	public static long BATTLE_EXPAND_TIME; // = 10 * 1000;
	
	static {
		BATTLE_BETWEEN_GRID_NUM = GlobalService.getInt("BATTLE_BETWEEN_GRID_NUM");
		BATTLE_ENEMY_TEAM_FACE = GlobalService.getByte("BATTLE_ENEMY_TEAM_FACE");
		BATTLE_MY_TEAM_FACE = GlobalService.getByte("BATTLE_MY_TEAM_FACE");
		BATTLE_DEF_FACTOR = GlobalService.getInt("BATTLE_DEF_FACTOR");
		BATTLE_EXPAND_TIME = GlobalService.getInt("BATTLE_EXPAND_TIME");		
	}
}
