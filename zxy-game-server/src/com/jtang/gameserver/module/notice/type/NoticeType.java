package com.jtang.gameserver.module.notice.type;


public enum NoticeType {
	/**
	 * 抢夺成功：X抢夺时击败了X
	 */
	SNATCH_SUCCESS(1),
	
	/**
	 * 最强势力排行：前3名奖励时要有全服广播：“最强势力第X名：[玩家名X]，今天得到[仙人名X]魂魄X个”
	 */
	POWER_RANK(2),
	
	/**
	 * 系统公告
	 */
	SYSTEM_NOTICE(3),
	
	/**
	 * 集众降魔
	 */
	DEMON_NOTICE(4),
	
	/**
	 * 跨服战
	 */
	CROSS_BATTLE(6);
	
	private byte code;
	
	private NoticeType(int code){
		this.code = (byte)code;
	}
	
	public byte getCode(){
		return code;
	}
}
