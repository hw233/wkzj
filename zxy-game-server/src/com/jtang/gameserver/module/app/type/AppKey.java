package com.jtang.gameserver.module.app.type;

import com.jiatang.common.model.DataType;

/**
 * 扩展字段枚举
 * 
 * @author ludd
 * 
 */
public enum AppKey {
	/**
	 * 可领次数
	 */
	REWARD_NUM("REWARD_NUM", DataType.INT),

	/**
	 * 已领次数
	 */
	HAD_REWARD_NUM("HAD_REWARD_NUM", DataType.INT),
	/**
	 * 充值金额
	 */
	RECHARGE_MONEY("RECHARGE_MONEY", DataType.INT),
	/**
	 * 物品id
	 */
	GOODS_ID("GOODS_ID", DataType.INT),
	/**
	 * 物品数量
	 */
	GOODS_NUM("GOODS_NUM", DataType.INT),
	/**
	 * 1_2_3_4|1_2_3_4
	 */
	APP_STRING("APP_STRING", DataType.STRING),
	/**
	 * 是否已领取 0.未领取 1.已领取
	 */
	IS_GET("IS_GET", DataType.INT),
	/**
	 * 最高等级角色id
	 */
	MAX_ACTOR_LEVEL("MAX_ACTOR_LEVEL", DataType.LONG),
	/**
	 * 最强势力榜第一角色id
	 */
	MAX_ACTOR_POWER("MAX_ACTOR_POWER",DataType.LONG),
	/**
	 * 奖励ID
	 */
	REWARD_ID("REWARD_ID", DataType.INT),
	/**
	 * 充值第一
	 */
	ONE_PAY_ACTOR("ONE_PAY_ACTOR",DataType.STRING),
	/**
	 * 充值第二ID
	 */
	TWO_PAY_ACTOR("TWO_PAY_ACTOR",DataType.STRING),
	/**
	 * 充值第三ID
	 */
	THREE_PAY_ACTOR("THREE_PAY_ACTOR",DataType.STRING),
	/**
	 * 连续登陆天数
	 */
	LOGIN_DAY("LOGIN_DAY",DataType.INT),
	/**
	 * 连续登陆时间
	 */
	LOGIN_TIME("LOGIN_TIME",DataType.INT),
	/**
	 * 电话号码
	 */
	PHONE_NUMBER("PHONE_NUMBER",DataType.STRING),
	
	/**
	 * 是否已达成 0.未达成  1.已达成  2.已完成   
	 */
	IS_FINISH("IS_FINISH",DataType.INT),
	
	/**
	 * 首次充值时间
	 */
	FIRST_RECHARGE_TIME("FIRST_RECHARGE_TIME", DataType.INT),
	
	/**
	 * 剩余时间
	 */
	REMAIN_TIME("REMAIN_TIME",DataType.INT),
	
	/**
	 * 消息
	 */
	MSG("MSG",DataType.STRING),
	
	/**
	 * 抢夺次数
	 */
	SNATCH_NUM("SNATCH_NUM",DataType.INT);
	
	
	private final String key;
	private final DataType dateType;

	private AppKey(String str, DataType dataType) {
		this.key = str;
		this.dateType = dataType;
	}

	public String getKey() {
		return key;
	}

	public DataType getDateType() {
		return dateType;
	}

}
