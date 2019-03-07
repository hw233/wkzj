package com.jtang.gameserver.module.dailytask.type;

public enum DailyTaskType {
	/**
	 * 
	 */
	NONE(0),
	
	/**
	 * 御赐小宴
	 */
	SUPPLY(1),
	/**
	 * 故事通关
	 */
	STORY_PASSED(2), 
	/**
	 * 斗法
	 */
	SNATCH(3), 
	/**
	 * 登天塔通关
	 */
	BABLE_SUCESS(4),
	/**
	 * 聚仙
	 */
	RECRUIT(5), 
	/**
	 * 送礼
	 */
	GIVE_GIFT(6),
	/**
	 * 收礼
	 */
	RECEIVE_GIFT(7),
	/**
	 * 盟友切磋
	 */
	ALLAY_PK(8),
	/**
	 * 装备精炼
	 */
	EQUIP_REFINE(9),
	/**
	 * 仙人潜修
	 */
	HERO_DELVE(10), 
	/**
	 * 洞府通关
	 */
	HOLE(11),
	/**
	 * 购买金币
	 */
	BUY(12),
	/**
	 * VIP等级限制
	 */
	VIP_COMPLETE(13),
	
	/**
	 * 合作关卡
	 */
	COOPERATE_STORY(15),
	/**
	 * 试练洞
	 */
	TRIALCAVE(16),
	/**
	 * 封神榜
	 */
	POWER(17),
	/**
	 * 抢夺次数
	 */
	SNATCH_NUM(18),
	
	/**
	 * 开启福神宝箱
	 */
	VIP_BOX(21),
	
	
	;
	
	
	private int code;
	private DailyTaskType(int code){
		this.code = code;
	}
	
	public int getCode(){
		return code;
	}

	public static DailyTaskType get(int type) {
		for(DailyTaskType taskType : DailyTaskType.values()){
			if(taskType.getCode() == type)
				return taskType;
		}
		return NONE;
	}
	
}
