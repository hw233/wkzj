package com.jtang.gameserver.module.user.model;


/**
 * vip11等级特权数据
 * @author ludd
 *
 */
public class Vip11Privilege extends Vip10Privilege{

	/**
	 * vip等级
	 */
	public static final int vipLevel = 11;
	

	@Override
	public int getVipLevel() {
		return vipLevel;
	}

	@Override
	public void init(String[] strArr) {
	}
	
	
	
}
