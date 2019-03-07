package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.battle.constant.WinLevel;

@DataFile(fileName = "trialBattleConfig")
public class TrialBattleConfig implements ModelAdapter {
	/**
	 * 战场id
	 */
	private int battleId;
	
	/**
	 * 地图id
	 */
	private int mapId;
	
	/**
	 * 依赖于故事的关卡id
	 */
	private int dependStoryBattle;
	
	/**
	 * 战场名称
	 */
	private String name;
	
	/**
	 * 奖励声望值,格式:大败1_声望值|失败2_声望值|惜败3_声望值|小胜4_声望值|中胜5_声望值|大胜6_声望值
	 */
	private String awardReputation;
	
	/**
	 * 奖励给盟友的声望值,格式:大败1_声望值|失败2_声望值|惜败3_声望值|小胜4_声望值|中胜5_声望值|大胜6_声望值
	 */
	private String awardAllyReputation;
	
	/**
	 * 奖励物品 格式:大败1_物品id_数量_掉落概率|失败2_数量_物品id_掉落概率|惜败3_物品id_数量_掉落概率|小胜4_物品id_数量_掉落概率|中胜5_物品id_数量_掉落概率|大胜6_物品id_数量_掉落概率
	 */
	private String awardGoods;
	
	/**
	 * 奖励给盟友的物品 格式:大败1_物品id_数量_掉落概率|失败2_数量_物品id_掉落概率|惜败3_物品id_数量_掉落概率|小胜4_物品id_数量_掉落概率|中胜5_物品id_数量_掉落概率|大胜6_物品id_数量_掉落概率
	 */
	private String awardAllyGoods;
	
	/**
	 * 最少怪物数量
	 */
	private int minMonsterCount;
	
	/**
	 * 怪物配置
	 */
	private String monsters;
	
	/**
	 * 气势
	 */
	public int morale;
		
	@FieldIgnore
	private Map<WinLevel, Integer> awardReputationMap = new HashMap<WinLevel, Integer>();
	
	@FieldIgnore
	private Map<WinLevel, Integer> awardAllyReputationMap = new HashMap<WinLevel, Integer>();
	
	@FieldIgnore
	private Map<WinLevel, List<AwardGoodsConfig>> awardGoodsMap = new HashMap<WinLevel, List<AwardGoodsConfig>>();
	
	@FieldIgnore
	private Map<WinLevel, List<AwardGoodsConfig>> awardAllyGoodsMap = new HashMap<WinLevel, List<AwardGoodsConfig>>();
	
	/**
	 * 怪物配置, 格式是Map<GridIndex, Map<MonsterId, Rate>>
	 */
	@FieldIgnore
	private Map<Integer, Map<Integer, Integer>> monsterMap = new HashMap<>();
	
	@Override
	public void initialize() {
		parseReputation(awardReputation, awardReputationMap);
		parseReputation(awardAllyReputation, awardAllyReputationMap);
		parseGoods(awardGoods, awardGoodsMap);
		parseGoods(awardAllyGoods, awardAllyGoodsMap);
		parseMonster();
		this.awardReputation = null;
		this.awardAllyReputation = null;
		this.awardGoods = null;
		this.awardAllyGoods = null;
	}
	
	private void parseMonster() {
		List<String> monsterList = StringUtils.delimiterString2List(monsters, Splitable.ELEMENT_SPLIT);
		for (String str : monsterList) {
			List<String> list = StringUtils.delimiterString2List(str, Splitable.ATTRIBUTE_SPLIT);
			Integer index = Integer.valueOf(list.get(0));
			Map<Integer, Integer> map = new HashMap<>();
			for (int i = 1; i < list.size(); i += 2) {
				Integer monsterId = Integer.valueOf(list.get(i));
				Integer rate = Integer.valueOf(list.get(i+1));
				map.put(monsterId, rate);
			}
			monsterMap.put(index, map);
		}
	}
	

	/**
	 * 将声望奖励字符串转化成实体
	 */
	private void parseReputation(String input, Map<WinLevel, Integer> output) {
		Map<Integer, Integer> map = StringUtils.delimiterString2IntMap(input);
		for (Entry<Integer, Integer> entry : map.entrySet()) {			
			//胜利的类型
			int level = entry.getKey();
			//奖励声望值
			int count = entry.getValue();
			WinLevel winLv = WinLevel.getByCode(level);
			output.put(winLv, count);
		}
	}
	
	/**
	 * 将物品奖励字符串转化成实体
	 */
	private void parseGoods(String input, Map<WinLevel, List<AwardGoodsConfig>> output) {		
		List<String> goodsList = StringUtils.delimiterString2List(input, Splitable.ELEMENT_SPLIT);
		for (String str : goodsList) {
			List<AwardGoodsConfig> list = new ArrayList<>();
			List<String> goods = StringUtils.delimiterString2List(str, Splitable.ATTRIBUTE_SPLIT);
			int level = Integer.valueOf(goods.get(0));
			for (int i = 1; i < goods.size(); i+=3) {
				int goodsId = Integer.valueOf(goods.get(i));
				int goodsNum = Integer.valueOf(goods.get(i+1));
				int rate = Integer.valueOf(goods.get(i+2));
				list.add(new AwardGoodsConfig(goodsId, goodsNum, rate));
				output.put(WinLevel.getByCode(level), list);
			}
		}
	}

	public int getBattleId() {
		return battleId;
	}

	public int getMapId() {
		return mapId;
	}

	public int getDependStoryBattle() {
		return dependStoryBattle;
	}

	public String getName() {
		return name;
	}


	public Map<WinLevel, Integer> getAwardReputationMap() {
		return awardReputationMap;
	}


	public Map<WinLevel, Integer> getAwardAllyReputationMap() {
		return awardAllyReputationMap;
	}


	public Map<WinLevel, List<AwardGoodsConfig>> getAwardGoodsMap() {
		return awardGoodsMap;
	}


	public Map<WinLevel, List<AwardGoodsConfig>> getAwardAllyGoodsMap() {
		return awardAllyGoodsMap;
	}


	public int getMinMonsterCount() {
		return minMonsterCount;
	}

	public Map<Integer, Map<Integer, Integer>> getMonsterMap() {
		return monsterMap;
	}
}
