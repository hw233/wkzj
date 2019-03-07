package com.jtang.gameserver.module.user.model;

public class Vip9Privilege extends Vip8Privilege {

	/**
	 * vip等级
	 */
	public static final int vipLevel = 9;
	
	@Override
	public int getVipLevel() {
		return vipLevel;
	}

	@Override
	public void init(String[] strArr) {
	}

}
