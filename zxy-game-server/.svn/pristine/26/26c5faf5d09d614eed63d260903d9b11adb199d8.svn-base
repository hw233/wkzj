package com.jtang.gameserver.module.goods.type;


public enum UseGoodsParserType {
	NONE(0),
	/**
	 * 精活丹使用
	 * effectValue: 增加的上限值_增加的当前值
	 */
	TYPE1(1),
	/**
	 * 宝箱使用
	 * <pre>
	 * effectValue:type(类型)_rate(概率)_expr(支持表达式)|...
	 * 如果是碎片expr为 碎片类型,星级(半角逗号隔开)
	 * 如果是物品 expr为 ID,NUM(半角逗号隔开)
	 * 如果是英雄魂魄 expr ID,Num(半角逗号隔开)
	 * </pre>
	 */
	TYPE2(2),
	
	/**
	 * 恢复精力
	 */
	TYPE3(3),
	/**
	 * 恢复活力
	 */
	TYPE4(4),
	/**
	 * 补充聚仙次数
	 */
	TYPE5(5),
	/**
	 * 金币包使用
	 * effectValue:x1*10 x1:表示掌教等级
	 */
	TYPE6(6),
	/**
	 * vip礼包使用
	 * effectValue:1_1_1|2_1_2 类型(1：金币，2：点券，3：英雄，4:物品)_ID_数量(支持表达式)
	 */
	TYPE7(7),
	/**
	 * 降魔积分丹使用
	 * effectValue:增加的降魔积分值
	 */
	TYPE8(8), 
	/**
	 * 精力丹（当前值和上限）
	 * effectValue: 增加的上限值_增加的当前值
	 */
	TYPE10(10), 
	
	/**
	 * 活力丹(当前值和上限)
	 * effectValue: 增加的上限值_增加的当前值
	 */
	TYPE11(11),
	
	/**
	 * 使用碎片
	 * effectValue:type(类型)_rate(概率)_expr(支持表达式)|...
	 */
	TYPE12(12),
	
	/**
	 * 增加上限并补满活力
	 */
	TYPE13(13),
	
	/**
	 * 增加上限并补满精力
	 */
	TYPE14(14),
	
	/**
	 * 类型15
	 * 多随一宝箱
	 */
	TYPE15(15),
	
	/**
	 * 类型16
	 * 20元话费,红包或者手机
	 */
	TYPE16(16),
	
	/**
	 * 类型17
	 * vip每日宝箱
	 */
	TYPE17(17);
	
	private final int type;
	private UseGoodsParserType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	
	public static UseGoodsParserType getUseGoodsParserType(int type){
		for (UseGoodsParserType useGoodsParserType : UseGoodsParserType.values()) {
			if (type == useGoodsParserType.getType()) {
				return useGoodsParserType;
			}
		}
		return UseGoodsParserType.NONE;
	}
	
	
}
