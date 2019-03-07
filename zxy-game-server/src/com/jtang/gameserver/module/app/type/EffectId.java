package com.jtang.gameserver.module.app.type;


/**
 * 活动唯一id枚举
 * @author 0x737263
 *
 */
public enum EffectId {
	/**
	 * 活动类型1 充值每达xx元送xx 
	 * <pre>
	 * appConfig.xml配置规则
	 * rewardGoods格式：类型_id_数量
	 * rule格式:充值金额
	 * </pre>
	 */
	EFFECT_ID_1(1),
	
	/**
	 * 活动类型2   用xxx个xxx兑换xxx个xxx
	 * <pre>
	 * appConfig.xml配置规则
	 * rewardGoods格式：
	 * rule格式:物品id_数量_兑换目标物品类型(0:物品，1：装备，2：仙人魂魄 ，3：金币)_兑换目标物品id_目标物品数量|...
	 * </pre>
	 */
	EFFECT_ID_2(2),
	
	/**
	 * 活动类型3 活动期间每天领取xxx
	 * rewardGoods格式:type_类型_数量
	 * rule格式:可领取次数
	 */
	EFFECT_ID_3(3),
	
	/**
	 * 活动类型5《一元礼包1》
	 * rewardGoods:
	 * rule格式:消耗点券_类型_id_数量_可购买次数
	 */
	EFFECT_ID_5(5),
	
	/**
	 * 活动类型6《记录活动期间 等级第一》
	 * rewardGoods:
	 * rule格式:开始时间(yyyy-MM-dd HH:mm:ss)_刷新间隔时间(分钟)
	 */
	EFFECT_ID_6(6),
	
	/**
	 * 活动类型7 活动期间充值前三名 结束后领取奖励
	 * rewardGoods:
	 * rule格式:名次_礼包id_数量|名次_礼包id_数量
	 */
	EFFECT_ID_7(7),
	
	/**
	 * 活动类型8 活动期间连续登陆7天领取奖励
	 * rewardGoods:
	 * rule格式:登陆天数_礼包id_数量|登陆天数_礼包id_数量
	 */
	EFFECT_ID_8(8),
	
	/**
	 * 活动类型9《升级送话费》
	 * rewardGoods:
	 * rule格式:活动类型_参数
	 * 1.达到x级送话费(参数填等级)
	 * 2.充值后记录电话,正式服返还双倍(参数填0)
	 */
	EFFECT_ID_9(9),
	
	/**
	 * 活动类型10 活动期间登陆送xx点券
	 * rewardGoods:
	 * rule格式:可领取的点券数
	 */
	EFFECT_ID_10(10),
	/**
	 * 活动类型11 首次充值后N小时内充值双倍
	 * rewardGoods:
	 * rule格式:点券翻倍数_持续时间(分钟)
	 */
	EFFECT_ID_11(11),
	
	/**
	 * 活动类型12 冲xx元领取奖励(一次)
	 * rewardGoods:type_id_数量
	 * rule格式:冲多少钱
	 */
	EFFECT_ID_12(12),
	
	/**
	 * 活动类型13 纯文字展示
	 * rewardGoods:
	 * rule格式:
	 */
	EFFECT_ID_13(13),
	
	/**
	 * 活动类型14 先消耗X点X次精力再消耗X点X次点券购买XX
	 * rewardGoods:
	 * rule格式:type_id_数量_可购买次数_消耗精力次数_消耗精力点_消耗点券点
	 */
	EFFECT_ID_14(14),
	
	/**
	 * 活动类型15 点券购买已有仙人魂魄
	 * rewardGoods:
	 * rule格式:仙人id_可购买次数_数量_购买公式|...
	 */
	EFFECT_ID_15(15),
	
	/**
	 * 活动类型16 活动期间充值可领取奖励(每天)
	 * rewardGoods:type_id_数量_几率
	 * rule格式:冲多少钱
	 */
	EFFECT_ID_16(16),
	
	/**
	 * 活动类型17《记录活动期间 封神榜第一》
	 * rewardGoods:
	 * rule格式:开始时间(yyyy-MM-dd HH:mm:ss)_刷新间隔时间(分钟)
	 */
	EFFECT_ID_17(17),
	
	/**
	 * 活动类型18《累计登陆X天领奖励》
	 * rweardGoods:
	 * rule格式:天数_奖励类型(RewardType),id,数量:...|...
	 */
	EFFECT_ID_18(18),
	
	/**
	 * 活动类型19《春节活动》
	 * rule格式:物品ID_ActorID_电话号码|物品ID_ActorID_电话号码|...
	 * 没有对应 的AppConfig所以单独处理
	 */
	EFFECT_ID_19(19),
	
	/**
	 * 活动类型20《新号首日冲级奖励》
	 * rewardGoods格式：类型_id_数量
	 * rule格式:玩家等级
	 */
	EFFECT_ID_20(20),
	
	
	;
	
	private final int id;

	private EffectId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static EffectId getById(int id) {
		for (EffectId key : values()) {
			if (id == key.getId()) {
				return key;
			}
		}
		return null;
	}
}
