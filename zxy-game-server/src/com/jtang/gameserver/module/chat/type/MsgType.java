package com.jtang.gameserver.module.chat.type;

public enum MsgType {
	/**
	 * 获取装备或仙人系统消息
	 */
	SYSTEM(1),

	/**
	 * 玩家聊天
	 */
	ACTOR(2),
	
	/**
	 * 抢夺系统消息
	 */
	SNATCH(3),
	
	/**
	 * 最强势力排行榜
	 */
	POWER(4),
	
	/**
	 * 开宝箱
	 */
	BOX(5),
	
	/**
	 * 集众降魔
	 */
	DEMON(7),
	
	/**
	 * 集众降魔获胜
	 */
	DEMON_WIN(8),
	
	/**
	 * 迷宫寻宝大奖消息
	 */
	MAZE_TREASURE(9),
	
	/**
	 * 仙人图鉴
	 */
	HERO_BOOK(10),
	
	/**
	 * 天财地宝
	 */
	TREASURE(11),
	
	/**
	 * 种植
	 */
	PLANT(12),
	
	/**
	 * 天宫寻宝
	 */
	WELKIN(13), 
	
	/**
	 * 天梯
	 */
	LADDER(14);

	private int code;

	private MsgType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
