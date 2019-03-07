package com.jtang.gameserver.module.user.model;

/**
 * vip2等级特权数据
 * @author ludd
 *
 */
public class Vip2Privilege extends Vip1Privilege {

	/**
	 * vip等级
	 */
	public static final int vipLevel = 2;


	@Override
	public int getVipLevel() {
		return vipLevel;
	}


	@Override
	public void init(String[] strArr) {
	}
	
	
}
