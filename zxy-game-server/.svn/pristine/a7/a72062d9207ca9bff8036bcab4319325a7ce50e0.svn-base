package com.jtang.gameserver.module.battle.type;


/**
 * 战斗类型
 * @author ludd
 *
 */
public enum BattleType {
	NONE(0),
	/**
	 * 故事战斗
	 */
	STORY(1),
	/**
	 * 登天塔战斗
	 */
	BABLE(2),
	/**
	 * 抢夺战斗
	 */
	SNATCH(3),
	/**
	 * 势力排行战斗
	 */
	POWER(4),
	/**
	 * 试练洞
	 */
	TRIAL(5),
	/**
	 * 城战
	 */
	CITYWAR(6),
	/**
	 * 盟友
	 */
	ALLY(7),
	/**
	 * 上古洞府
	 */
	HOLE(8),
	/**
	 * 集众降魔攻击boss
	 */
	DEMON_ATTACK_BOSS(9),
	/**
	 * 集众降魔攻击玩家
	 */
	DEMON_ATTACK_PLAYER(10),
	/**
	 * 迷宫寻宝战斗 
	 */
	TREASURE_BATTLE(11),
	/**
	 * 跨服战
	 */
	CROSS_BATTLE(12), 
	/**
	 * 天梯
	 */
	LADDER(13),
	/**
	 * 结婚排行
	 */
	LOVE_FIGHT(14),
	/**
	 *年兽 
	 */
	BEAST(15);
	
	private final int code;
	private BattleType(int type) {
		this.code  = type;
	}
	public int getCode() {
		return code;
	}
	
	public static BattleType getByCode(int code) {
		for (BattleType key : BattleType.values()) {
			if (key.getCode() == code) {
				return key;
			}
		}
		return NONE;
	}
	
}
