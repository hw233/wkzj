package com.jtang.gameserver.module.app.model.extension.rulevo;


public class ExchangeLoginVO {
	/**
	 * 登陆天数
	 */
	public int loginDay;
	
	/**
	 * 礼包id
	 */
	public int goodsId;
	
	/**
	 * 礼包数量
	 */
	public int num;
	
	public static ExchangeLoginVO valueOf(String[] strings) {
		ExchangeLoginVO loginVO = new ExchangeLoginVO();
		loginVO.loginDay = Integer.valueOf(strings[0]);
		loginVO.goodsId = Integer.valueOf(strings[1]);
		loginVO.num = Integer.valueOf(strings[2]);
		return loginVO;
	}
	
}
