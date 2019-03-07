package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 签到奖励配置
 * */
@DataFile(fileName = "signRewardConfig")
public class SignRewardConfig implements ModelAdapter {

	/**
	 * 月份
	 */
	public int month;
	
	/**
	 * 天数
	 */
	public int day;
	
	/**
	 * 奖励类型
	 */
	public int rewardType;
	
	/**
	 * 奖励id
	 */
	public int rewardId;
	
	/**
	 * 奖励数量
	 */
	public int rewardNum;
	
	/**
	 * vip等级
	 */
	public int vipLevel;
	
	/**
	 * vip领取数量
	 */
	public int vipNum;
	
	/**
	 * 豪华签到
	 * 0.不是 1.是
	 */
	public int luxury;

	@Override
	public void initialize() {
		
	}

	public boolean isLuxury() {
		return luxury == 1 ? true : false;
	}

}
