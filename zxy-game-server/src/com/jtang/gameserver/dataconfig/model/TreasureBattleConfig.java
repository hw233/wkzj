package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.battle.constant.WinLevel;

@DataFile(fileName = "treasureBattleConfig")
public class TreasureBattleConfig implements ModelAdapter {

	/**
	 * 战场id
	 */
	public int battleId;

	/**
	 * 地图Id
	 */
	public int mapId;

	/**
	 * 奖励声望值
	 */
	public String awardReputation;

	/**
	 * 奖励仙人经验值
	 */
	public String awardHeroExp;

	/**
	 * 气势
	 */
	public String morale;

	/**
	 * 奖励
	 */
	public String reward;

	@FieldIgnore
	private Map<Integer, Integer> monsterMap = new HashMap<Integer, Integer>();// 怪物列表map

	@FieldIgnore
	private List<RewardConfig> rewardObject = new ArrayList<>();// 奖励

	@FieldIgnore
	private Map<WinLevel, String> rewardsReputation = new HashMap<>();// 奖励声望map

	@FieldIgnore
	private Map<WinLevel, String> rewardsHeroExp = new HashMap<>();// 奖励经验map

	@Override
	public void initialize() {

		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for (String[] array : list) {
			RewardConfig reward = new RewardConfig(Arrays.asList(array));
			rewardObject.add(reward);
		}

		List<String[]> reputation = StringUtils.delimiterString2Array(awardReputation);
		if(reputation.size() != 0){
			for (String[] item : reputation) {
				WinLevel winLevel = WinLevel.getByCode(Integer.valueOf(item[0]));
				rewardsReputation.put(winLevel, item[1]);
			}
		}

		List<String[]> heroExp = StringUtils.delimiterString2Array(awardHeroExp);
		if(heroExp.size() != 0){
			for (String[] item : heroExp) {
				WinLevel winLevel = WinLevel.getByCode(Integer.valueOf(item[0]));
				rewardsHeroExp.put(winLevel, item[1]);
			}
		}
		
		this.reward = null;
		this.awardReputation = null;
		this.awardHeroExp = null;
	}
	
	public String getReputation(WinLevel winLevel){
		return rewardsReputation.get(winLevel);
	}
	
	public String getHeroExp(WinLevel winLevel){
		return rewardsHeroExp.get(winLevel);
	}
	
	public Map<Integer, Integer> getMonsters() {
		return monsterMap;
	}
	
	public RewardConfig getReward(){
		Map<Integer, Integer> map = new HashMap<>();
		if(rewardObject.size() == 0){
			return null;
		}
		for (int i = 0; i < rewardObject.size(); i++) {
			map.put(i, rewardObject.get(i).proportion);
		}
		Integer index = RandomUtils.randomHit(1000, map);
		if(index == null){
			return null;
		}
		return rewardObject.get(index);
	}

}
