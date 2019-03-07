package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.BableBattleConfig;
import com.jtang.gameserver.dataconfig.model.BableConfig;
import com.jtang.gameserver.dataconfig.model.BableExchangeConfig;
import com.jtang.gameserver.dataconfig.model.BableMonsterConfig;
import com.jtang.gameserver.dataconfig.model.BableMonsterNumConfig;
import com.jtang.gameserver.dataconfig.model.BableRankRewardConfig;
import com.jtang.gameserver.dataconfig.model.BableSkipConfig;
import com.jtang.gameserver.module.adventures.bable.model.BableExchangeVO;

/**
 * 通天塔数据配置服务类
 * @author 0x737263
 *
 */
@Component
public class BableService extends ServiceAdapter {

	/** key:登天塔类型 */
	private static Map<Integer,BableConfig> BABLE_CONFIG_MAP = new HashMap<>();
	
	/** <key:登天塔类型 value:<key:位置 value:配置list>> */
	private static Map<Integer,Map<Integer,List<BableBattleConfig>>> BABLE_BATTLE_MAP = new HashMap<>();
	
	/** key:物品id */
	private static Map<Integer,Map<Integer,BableExchangeConfig>> BABLE_EXCHANGE_MAP = new HashMap<>();
	
	/** key:登天塔类型 value:<key:层数 value:配置>> */
	private static Map<Integer,Map<Integer,BableMonsterConfig>> BABLE_MONSTER_MAP = new HashMap<>();
	
	/** key:名次 */
	private static Map<Integer,BableRankRewardConfig> BABLE_RANK_MAP = new HashMap<>();
	
	/** key:登天塔类型 */
	private static Map<Integer,BableSkipConfig> BABLE_SKIP_MAP = new HashMap<>();
	
	/** 登天塔层数怪物 */
	private static Map<Integer,List<BableMonsterNumConfig>> BABLE_MONSTER_NUM_MAP = new HashMap<>();
	
	private static int maxFloor = 100;
	@Override
	public void clear() {
		BABLE_CONFIG_MAP.clear();
		BABLE_BATTLE_MAP.clear();
		BABLE_EXCHANGE_MAP.clear();
		BABLE_MONSTER_MAP.clear();
		BABLE_RANK_MAP.clear();
		BABLE_SKIP_MAP.clear();
		BABLE_MONSTER_NUM_MAP.clear();
	}

	@Override
	public void initialize() {
		
		List<BableConfig> bableList = dataConfig.listAll(this, BableConfig.class);
		for(BableConfig bableConfig:bableList){
			BABLE_CONFIG_MAP.put(bableConfig.bableId, bableConfig);
		}
		
		List<BableBattleConfig> battleList = dataConfig.listAll(this, BableBattleConfig.class);
		for(BableBattleConfig battleConfig:battleList){
			if(BABLE_BATTLE_MAP.containsKey(battleConfig.bableType)){
				if(BABLE_BATTLE_MAP.get(battleConfig.bableType).containsKey(battleConfig.index)){
					BABLE_BATTLE_MAP.get(battleConfig.bableType).get(battleConfig.index).add(battleConfig);
				}else{
					List<BableBattleConfig> list = new ArrayList<>();
					list.add(battleConfig);
					BABLE_BATTLE_MAP.get(battleConfig.bableType).put(battleConfig.index, list);
				}
			}else{
				Map<Integer,List<BableBattleConfig>> map = new HashMap<>();
				List<BableBattleConfig> list = new ArrayList<>();
				list.add(battleConfig);
				map.put(battleConfig.index, list);
				BABLE_BATTLE_MAP.put(battleConfig.bableType, map);
			}
		}
		
		List<BableExchangeConfig> exchangeList = dataConfig.listAll(this, BableExchangeConfig.class);
		for(BableExchangeConfig exchangeConfig:exchangeList){
			if(BABLE_EXCHANGE_MAP.containsKey(exchangeConfig.bableType)){
				BABLE_EXCHANGE_MAP.get(exchangeConfig.bableType).put(exchangeConfig.exchangeId, exchangeConfig);
			}else{
				Map<Integer,BableExchangeConfig> map = new HashMap<>();
				map.put(exchangeConfig.exchangeId, exchangeConfig);
				BABLE_EXCHANGE_MAP.put(exchangeConfig.bableType, map);
			}
		}
		
		List<BableMonsterConfig> monsterList = dataConfig.listAll(this, BableMonsterConfig.class);
		for(BableMonsterConfig monsterConfig:monsterList){
			if(BABLE_MONSTER_MAP.containsKey(monsterConfig.bableType)){
				BABLE_MONSTER_MAP.get(monsterConfig.bableType).put(monsterConfig.floor, monsterConfig);
			}else{
				Map<Integer,BableMonsterConfig> map = new HashMap<>();
				map.put(monsterConfig.floor, monsterConfig);
				BABLE_MONSTER_MAP.put(monsterConfig.bableType, map);
			}
			maxFloor = maxFloor < monsterConfig.floor ? monsterConfig.floor : maxFloor;
		}
		
		List<BableRankRewardConfig> rankList = dataConfig.listAll(this, BableRankRewardConfig.class);
		for(BableRankRewardConfig rankConfig:rankList){
			BABLE_RANK_MAP.put(rankConfig.bableType, rankConfig);
		}
		
		List<BableSkipConfig> skipList = dataConfig.listAll(this, BableSkipConfig.class);
		for(BableSkipConfig skipConfig:skipList){
			BABLE_SKIP_MAP.put(skipConfig.getBableId(), skipConfig);
		}
		
		List<BableMonsterNumConfig> monsterNumList = dataConfig.listAll(this, BableMonsterNumConfig.class);
		for(BableMonsterNumConfig monsterNumConfig:monsterNumList){
			if(BABLE_MONSTER_NUM_MAP.containsKey(monsterNumConfig.bableType)){
				BABLE_MONSTER_NUM_MAP.get(monsterNumConfig.bableType).add(monsterNumConfig);
			}else{
				List<BableMonsterNumConfig> list = new ArrayList<>();
				list.add(monsterNumConfig);
				BABLE_MONSTER_NUM_MAP.put(monsterNumConfig.bableType,list);
			}
		}
		
	}

	public static BableConfig getBableConfig(int bableType) {
		return BABLE_CONFIG_MAP.get(bableType);
	}


	public static BableExchangeConfig getExchangeConfig(int bableType, int exchangeId) {
		return BABLE_EXCHANGE_MAP.get(bableType).get(exchangeId);
	}

	/**
	 * 随机登天塔兑换奖励列表
	 * @param bableId
	 * @return
	 */
	public static Map<Integer, BableExchangeVO> getExchangeList(int bableId) {
		List<BableExchangeConfig> randomList = new ArrayList<>(); 
		Map<Integer,BableExchangeVO> map = new HashMap<>();
		for(BableExchangeConfig exchangeConfig : BABLE_EXCHANGE_MAP.get(bableId).values()){
			if(exchangeConfig.proportion < 1000){
				randomList.add(exchangeConfig);
			}else{
				BableExchangeVO bableExchangeVO = new BableExchangeVO(exchangeConfig);
				map.put(bableExchangeVO.exchangeId, bableExchangeVO);
			}
		}
		if(randomList.size() > 0){
			for(BableExchangeConfig config:randomList){
				if(config.proportion > RandomUtils.nextInt(0, 1000)){
					BableExchangeVO bableExchangeVO = new BableExchangeVO(config);
					map.put(bableExchangeVO.exchangeId, bableExchangeVO);
				}
			}
		}
		return map;
	}

	public static Set<Integer> getBABLE_ID_LIST() {
		return BABLE_CONFIG_MAP.keySet();
	}

	public static BableMonsterConfig getBableMonsterConfig(int type, int floor) {
		floor = floor > maxFloor ? maxFloor : floor;
		return BABLE_MONSTER_MAP.get(type).get(floor);
	}

	/**
	 * 获取随机怪物
	 * @return
	 */
	public static Map<Integer, Integer> getMonsters(int type, int floor) {
		floor = floor > maxFloor ? maxFloor : floor;
		BableMonsterConfig monsterConfig = BABLE_MONSTER_MAP.get(type).get(floor);
		if(monsterConfig.getMonster().isEmpty() == false){
			return monsterConfig.getMonster();
		}
		Map<Integer,Integer> map = new HashMap<>();
		List<BableMonsterNumConfig> monsterNumList = BABLE_MONSTER_NUM_MAP.get(type);
		BableMonsterNumConfig monsterNumConfig = null;
 		for(BableMonsterNumConfig config : monsterNumList){
			if(config.floorMin <= floor && floor <= config.floorMax){
				monsterNumConfig = config;
				break;
			}
		}
 		int num1 = FormulaHelper.executeCeilInt(monsterNumConfig.monsterIndex1, type,floor);
 		int num2 = FormulaHelper.executeCeilInt(monsterNumConfig.monsterIndex2, type,floor);
 		int num3 = FormulaHelper.executeCeilInt(monsterNumConfig.monsterIndex3, type,floor);
 		map.putAll(getMonsterMap(type,num1,1));
 		map.putAll(getMonsterMap(type,num2,2));
 		map.putAll(getMonsterMap(type,num3,3));
		return map;
	}

	/**
	 * 获取每一个位置的怪物
	 * @param type 登天塔类型
	 * @param monsterNum 怪物数量
	 * @param index 位置(1.前<1/2/3>2.中<4/5/6>3.后<7/8/9>)
	 * @return
	 */
	private static Map<Integer,Integer> getMonsterMap(int type, int monsterNum, int index) {
		Map<Integer,Integer> map = new HashMap<>();
		List<BableBattleConfig> monsterList = BABLE_BATTLE_MAP.get(type).get(index);
		int[] array1 = RandomUtils.uniqueRandom(monsterNum, 0, monsterList.size() - 1);
		int[] array2;
		if(index == 1){
			array2 = RandomUtils.uniqueRandom(monsterNum, 1, 3);
		}else if(index == 2){
			array2 = RandomUtils.uniqueRandom(monsterNum, 4, 6);
		}else{
			array2 = RandomUtils.uniqueRandom(monsterNum, 7, 9);
		}
		for(int i = 0;i < array1.length;i++){
			BableBattleConfig battleConfig = monsterList.get(array1[i]);
			map.put(array2[i], battleConfig.monsterId);
		}
		return map;
	}
	
	public static int getMaxFloor() {
		return maxFloor;
	}
	
}
