package com.jtang.worldserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 整个赛季结束后奖励配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "crossBattleEndAwardConfig")
public class CrossBattleEndAwardConfig implements ModelAdapter {

	/**
	 * 日奖励唯一id
	 */
	private int awardId;
	
	/**
	 * 允许前x名服务器 领取该条奖励
	 */
	private int allowRank;
	
	/**
	 * 允许前x级的角色(allowRank为前置条件)
	 */
	private int allowLevel;
	
	/**
	 * 胜利服的玩家奖励物品 格式: 类型(0:物品，1：装备，2：仙人魂魄 ，3：金币）_物品id_物品数量表达式(x1*1000, x1:表示等级)|后面多个奖励
	 */
	private String reward;
	
	
	@Override
	public void initialize() {
		
	}

	public int getAwardId() {
		return awardId;
	}

	public int getAllowRank() {
		return allowRank;
	}

	public int getAllowLevel() {
		return allowLevel;
	}
	
	
	public String getReward() {
		return reward;
	}

}
