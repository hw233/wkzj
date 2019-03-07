package com.jtang.gameserver.module.app.model.extension.rulevo;


public class ExchangeBuyVO {
	/**
	 * 消耗点券
	 */
	public int costTicket;

	/**
	 * 类型 (0.物品1.装备2.仙人魂魄3.金币)
	 */
	public int type;

	/**
	 * 道具id
	 */
	public int id;

	/**
	 * 数量
	 */
	public int num;

	/**
	 * 可购买次数
	 */
	public int buyNum;

	public static ExchangeBuyVO valueOf(String[] strings) {
		ExchangeBuyVO buyGlobalVO = new ExchangeBuyVO();
		buyGlobalVO.costTicket = Integer.valueOf(strings[0]);
		buyGlobalVO.type = Integer.valueOf(strings[1]);
		buyGlobalVO.id = Integer.valueOf(strings[2]);
		buyGlobalVO.num = Integer.valueOf(strings[3]);
		buyGlobalVO.buyNum = Integer.valueOf(strings[4]);
		return buyGlobalVO;
	}
}
