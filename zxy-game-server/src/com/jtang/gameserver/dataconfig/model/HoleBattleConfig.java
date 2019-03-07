package com.jtang.gameserver.dataconfig.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.battle.constant.WinLevel;

/**
 * 洞府战场配置
 * 
 * @author jianglf
 * 
 */
@DataFile(fileName = "holeBattleConfig")
public class HoleBattleConfig implements ModelAdapter {
	/**
	 * 洞府战场id
	 */
	public int holeBattleId;

	/**
	 * 名字
	 */
	public String name;

	/**
	 * 地图
	 */
	public int map;

	/**
	 * 怪物id和位置 id1_位置(1-9)|id2_位置
	 */
	public String monster;
	
	/**
	 * 奖励声望
	 */
	public String awardReputation;
	
	/**
	 * 奖励仙人经验
	 */
	public String awardHeroExp;

	/**
	 *角色实际等级高于该值时所得的声望会减少
	 */
	public int defaultActorLevel;
	
	/**
	 * 怪物气势
	 */
	public int morale;

	/**
	 * 奖励: 小胜4_类型_物品id_数量|中胜5_类型_物品id_数量|大胜6_类型_物品id_数量
	 * 类型: 0.物品 1.装备 2.魂魄 3.金币
	 */
	public String reward;

	@FieldIgnore
	private Map<Integer, Integer> monsterMap = new HashMap<Integer, Integer>();// 怪物列表map
	
	@FieldIgnore
	private Map<WinLevel,HoleRewardConfig> rewardsMap = new HashMap<WinLevel,HoleRewardConfig>();//奖励map
	
	@FieldIgnore
	private Map<WinLevel,Integer> rewardsReputation = new HashMap<>();//奖励声望map
	
	@FieldIgnore
	private Map<WinLevel,Integer> rewardsHeroExp = new HashMap<>();//奖励经验map

	@Override
	public void initialize() {
		parseMonsters();
		
		List<String[]> rewards = StringUtils.delimiterString2Array(reward);
		for(String[] item:rewards){
			WinLevel winLevel = WinLevel.getByCode(Integer.valueOf(item[0]));
			rewardsMap.put(winLevel, HoleRewardConfig.parseHoleBattleReward(item));
		}
		
		List<String[]> reputation = StringUtils.delimiterString2Array(awardReputation);
		for(String[] item:reputation){
			WinLevel winLevel = WinLevel.getByCode(Integer.valueOf(item[0]));
			rewardsReputation.put(winLevel, Integer.valueOf(item[1]));
		}
		
		List<String[]> heroExp = StringUtils.delimiterString2Array(awardHeroExp);
		for(String[] item:heroExp){
			WinLevel winLevel = WinLevel.getByCode(Integer.valueOf(item[0]));
			rewardsHeroExp.put(winLevel, Integer.valueOf(item[1]));
		}
		
		this.monster = null;
		this.reward = null;
		this.awardReputation = null;
		this.awardHeroExp = null;
	}

	/**
	 * 将怪物字符串转化成实体列表
	 */
	private void parseMonsters() {
		List<String> list = StringUtils.delimiterString2List(monster, Splitable.ELEMENT_SPLIT);
		for (String item : list) {
			List<String> attrs = StringUtils.delimiterString2List(item, Splitable.ATTRIBUTE_SPLIT);
			// 仙人id
			int heroId = Integer.valueOf(attrs.get(0));
			// 仙人在阵型中的位置
			int gridIndex = Integer.valueOf(attrs.get(1));
			monsterMap.put(gridIndex, heroId);
		}
	}

	public Map<Integer, Integer> getMonsters() {
		return monsterMap;
	}
	
	public HoleRewardConfig getReward(WinLevel winLevel){
		if(rewardsMap.isEmpty()){
			return null;
		}
		int proportion = rewardsMap.get(winLevel).proportion;
		int number = RandomUtils.nextInt(0, 1000);
		if(proportion >= number){
			return rewardsMap.get(winLevel);
		}
		return null;
	}
	
	public int getReputation(WinLevel winLevel){
		if(rewardsReputation.isEmpty()){
			return 0;
		}
		return rewardsReputation.get(winLevel);
	}
	
	public int getHeroExp(WinLevel winLevel){
		if(rewardsHeroExp.isEmpty()){
			return 0;
		}
		return rewardsHeroExp.get(winLevel);
	}

}