package com.jtang.gameserver.module.goods.type;

/**
 * 一般物品子分类
 * 1表示精力药剂，2表示活力药剂，3表示精活仙丹，4表示铜钥匙，5表示银钥匙  6 表示金钥匙 
 * @author 0x737263
 *
 */
public interface BaseSubType {
	
	/**
	 * 精力药剂
	 */
	int ENERGY_DRUG = 1;

	/**
	 * 活力药剂
	 */
	int VIT_DRUG = 2;
	
	/**
	 * 精活仙丹
	 */
	int ENERGY_VIT_DRUG = 3;
	
	/**
	 * 铜钥匙
	 */
	int COPPER_KEY = 4;
	
	/**
	 * 银钥匙
	 */
	int SILVER_KEY = 5;
	
	/**
	 * 金钥匙
	 */
	int GOLD_KEY = 6;
}
