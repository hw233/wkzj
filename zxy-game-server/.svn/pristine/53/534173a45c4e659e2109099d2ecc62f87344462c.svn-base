package com.jtang.gameserver.dataconfig.model;


/**
 * 洞府通关单个奖励
 * @author jianglf
 *
 */
public class HoleRewardConfig {

	public int type;
	
	public int id;
	
	public int num;
	
	public int proportion;


	public static HoleRewardConfig parseAllyGift(String[] str) {
		HoleRewardConfig reward = new HoleRewardConfig();
		reward.type = Integer.valueOf(str[0]);
		reward.id = Integer.valueOf(str[1]);
		reward.num = Integer.valueOf(str[2]);
		return reward;
	}
	
	public static HoleRewardConfig parseHoleReward(String[] str){
		HoleRewardConfig reward = new HoleRewardConfig();
		reward.type = Integer.valueOf(str[1]);
		reward.id = Integer.valueOf(str[2]);
		reward.num = Integer.valueOf(str[3]);
		return reward;
	}
	
	public static HoleRewardConfig parseHoleBattleReward(String[] str){
		HoleRewardConfig reward = new HoleRewardConfig();
		reward.type = Integer.valueOf(str[1]);
		reward.id = Integer.valueOf(str[2]);
		reward.num = Integer.valueOf(str[3]);
		reward.proportion = Integer.valueOf(str[4]);
		return reward;
	}

}
