package com.jtang.gameserver.module.goods.type;

/**
 * 碎片物品子分类
 * 1表示武器，2表示防具，3表示饰品，4表示铜钥匙碎片，5表示银钥匙碎片,6表示金钥匙碎片，如果不是则填0
 * @author 0x737263
 *
 */
public interface PieceSubType {

	/**
	 * 武器 碎片
	 */
	int WEAPON_PIECE = 1;

	/**
	 * 防具碎片
	 */
	int ARMOR_PIECE = 2;

	/**
	 * 饰品碎片
	 */
	int ORNAMENTS_PIECE = 3;

	/**
	 * 铜钥匙碎片
	 */
	int COPPER_KEY_PIECE = 4;

	/**
	 * 银钥匙碎片
	 */
	int SILVER_KEY_PIECE = 5;

	/**
	 * 金钥匙碎片
	 */
	int GOLD_KEY_PIECE = 6;

}
