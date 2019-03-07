package com.jtang.gameserver.module.user.model;

/**
 * vip1等级特权数据
 * @author ludd
 *
 */
public class Vip1Privilege extends VipPrivilege {

	/**
	 * vip等级
	 */
	public static final int vipLevel = 1;
	
	

	@Override
	public int getVipLevel() {
		return vipLevel;
	}


	@Override
	public void init(String[] strArr) {
	}
	
}
