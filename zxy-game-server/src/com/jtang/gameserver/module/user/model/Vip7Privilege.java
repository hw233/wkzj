package com.jtang.gameserver.module.user.model;


/**
 * vip7等级特权数据
 * @author ludd
 *
 */
public class Vip7Privilege extends Vip6Privilege{

	/**
	 * vip等级
	 */
	public static final int vipLevel = 7;
	
	@Override
	public int getVipLevel() {
		return vipLevel;
	}


	@Override
	public void init(String[] strArr) {
	}
	
	
	
}
