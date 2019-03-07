package com.jtang.gameserver.module.user.model;


/**
 * vip3等级特权数据
 * @author ludd
 *
 */
public class Vip3Privilege extends Vip2Privilege {

	/**
	 * vip等级
	 */
	public static final int vipLevel = 3;


	@Override
	public int getVipLevel() {
		return vipLevel;
	}
	
	@Override
	public void init(String[] strArr) {
	}
	
	
}
