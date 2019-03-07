package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName="loveMonsterGlobalConfig")
public class LoveMonsterGlobalConfig implements ModelAdapter  {

	/**
	 * 难度
	 */
	public int difficultId;
	
	/**
	 * 地图id
	 */
	public int map;
	
	/**
	 * 解锁扣除物品id
	 */
	public int costGoods;
	
	/**
	 * 解锁扣除物品数量
	 */
	public int costNum;
	
	/**
	 * 解锁扣除点券
	 */
	public int costTicket;
	
	/**
	 * 每天可挑战次数
	 */
	public int fightNum;
	
	/**
	 * boss死亡掉落
	 */
	public String reward;
	
	/**
	 * 每次攻击掉落
	 */
	public String mustReward;
	
	@FieldIgnore
	public List<RewardObject> rewardList = new ArrayList<>();
	
	@FieldIgnore
	public List<RewardObject> mustRewardList = new ArrayList<>();
	
	@Override
	public void initialize() {
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for(String[] str : list){
			RewardObject rewardObject = RewardObject.valueOf(str);
			rewardList.add(rewardObject);
		}
		
		List<String[]> mustList = StringUtils.delimiterString2Array(mustReward);
		for(String[] str : mustList){
			RewardObject rewardObject = RewardObject.valueOf(str);
			mustRewardList.add(rewardObject);
		}
		
		reward = null;
		mustReward = null;
	}

}
