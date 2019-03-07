package com.jtang.gameserver.module.user.model;


/**
 * vip13等级特权数据
 * @author ludd
 *
 */
public class Vip13Privilege extends Vip12Privilege{

	/**
	 * vip等级
	 */
	public static final int vipLevel = 13;
	

	@Override
	public int getVipLevel() {
		return vipLevel;
	}


	@Override
	public void init(String[] strArr) {
	}
	
	
	
}
