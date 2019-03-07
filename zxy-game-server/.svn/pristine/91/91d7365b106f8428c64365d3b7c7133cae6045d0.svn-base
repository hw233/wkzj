package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.RewardConfig;
import com.jtang.gameserver.dataconfig.model.TreasureBattleConfig;
import com.jtang.gameserver.dataconfig.model.TreasureConfig;
import com.jtang.gameserver.dataconfig.model.TreasureExchangeConfig;
import com.jtang.gameserver.dataconfig.model.TreasureGlobalConfig;
import com.jtang.gameserver.dataconfig.model.TreasureLevelConfig;
import com.jtang.gameserver.dataconfig.model.TreasureMonsterConfig;
import com.jtang.gameserver.module.demon.model.OpenTime;
import com.jtang.gameserver.module.treasure.model.GridVO;

@Component
public class TreasureService extends ServiceAdapter {

	private static List<OpenTime> openTimes = new ArrayList<>();

	private static Map<String, TreasureConfig> TREASURE_CONFIG_MAP = new HashMap<>();

	private static Map<Integer, TreasureBattleConfig> TREASURE_BATTLE_CONFIG = new HashMap<>();

	private static List<TreasureGlobalConfig> CONFIG_LIST = new ArrayList<>();

	private static Map<String, List<TreasureLevelConfig>> TREASURE_LEVEL_CONFIG = new HashMap<>();

	private static Map<Integer, TreasureMonsterConfig> TREASURE_MONSTER_CONFIG = new HashMap<>();

	private static Map<Integer, TreasureExchangeConfig> TREASURE_EXCHANGE_CONFIG = new HashMap<>();

	@Override
	public void clear() {
		TREASURE_CONFIG_MAP.clear();
		TREASURE_BATTLE_CONFIG.clear();
		CONFIG_LIST.clear();
		openTimes.clear();
		TREASURE_LEVEL_CONFIG.clear();
		TREASURE_MONSTER_CONFIG.clear();
		TREASURE_EXCHANGE_CONFIG.clear();
	}

	@Override
	public void initialize() {
		List<TreasureConfig> treasureList = dataConfig.listAll(this, TreasureConfig.class);
		for (TreasureConfig config : treasureList) {
			String key = parseKey(config.gridX, config.gridY);
			TREASURE_CONFIG_MAP.put(key, config);
		}

		List<TreasureBattleConfig> battleList = dataConfig.listAll(this, TreasureBattleConfig.class);
		for (TreasureBattleConfig config : battleList) {
			TREASURE_BATTLE_CONFIG.put(config.battleId, config);
		}

		List<TreasureGlobalConfig> configList = dataConfig.listAll(this, TreasureGlobalConfig.class);
		for (TreasureGlobalConfig treasureGlobalConfig : configList) {
			List<String[]> timeList = StringUtils.delimiterString2Array(treasureGlobalConfig.openTime);
			for (String[] timestr : timeList) {
				OpenTime openTime = new OpenTime(timestr);
				openTimes.add(openTime);
			}
			CONFIG_LIST.add(treasureGlobalConfig);
		}

		List<TreasureLevelConfig> levelList = dataConfig.listAll(this, TreasureLevelConfig.class);
		for (TreasureLevelConfig treasureLevelConfig : levelList) {
			StringBuffer sb = new StringBuffer();
			sb.append(treasureLevelConfig.beginLevel);
			sb.append(Splitable.ATTRIBUTE_SPLIT);
			sb.append(treasureLevelConfig.endLevel);
			if (TREASURE_LEVEL_CONFIG.containsKey(sb.toString())) {
				TREASURE_LEVEL_CONFIG.get(sb.toString()).add(treasureLevelConfig);
			} else {
				List<TreasureLevelConfig> list = new ArrayList<>();
				list.add(treasureLevelConfig);
				TREASURE_LEVEL_CONFIG.put(sb.toString(), list);
			}
		}

		List<TreasureMonsterConfig> monsterList = dataConfig.listAll(this, TreasureMonsterConfig.class);
		for (TreasureMonsterConfig treasureMonsterConfig : monsterList) {
			TREASURE_MONSTER_CONFIG.put(treasureMonsterConfig.monsterId, treasureMonsterConfig);
		}

		List<TreasureExchangeConfig> exchangeList = dataConfig.listAll(this, TreasureExchangeConfig.class);
		for (TreasureExchangeConfig treasureExchangeConfig : exchangeList) {
			TREASURE_EXCHANGE_CONFIG.put(treasureExchangeConfig.exchangeId, treasureExchangeConfig);
		}
	}

	public static String parseKey(int gridX, int gridY) {
		return gridX + Splitable.ATTRIBUTE_SPLIT + gridY;
	}

	/**
	 * 获取初始化的迷宫地图
	 * 
	 * @return
	 */
	public static Map<String, GridVO> getGridMap(int level) {
		Map<String, GridVO> map = new HashMap<>();
		for (Entry<String, TreasureConfig> entry : TREASURE_CONFIG_MAP.entrySet()) {
			TreasureConfig config = entry.getValue();
			map.put(entry.getKey(), GridVO.valueOf(config,level));
		}
		int proportion = 0;
		int num = RandomUtils.nextInt(0, 1000);
		boolean isBigReward = true;
		for (Entry<String, TreasureConfig> entry : TREASURE_CONFIG_MAP.entrySet()) {// 设置大奖
			RewardConfig config = entry.getValue().getBigReward();
			proportion += config.proportion;
			if (num < proportion && isBigReward) {
				int rewardNum = FormulaHelper.executeCeilInt(config.num, level);
				map.get(entry.getKey()).rewardObject = new RewardObject(RewardType.getType(config.type), config.rewardId, rewardNum);
				map.get(entry.getKey()).isBigGift = 1;
				isBigReward = false;
			}
		}
		return map;
	}

	/**
	 * 获取怪物配置
	 */

	public static TreasureBattleConfig getBattleConfig(int battleId) {
		return TREASURE_BATTLE_CONFIG.get(battleId);
	}

	/**
	 * 获取迷宫规则配置
	 */
	public static TreasureGlobalConfig getMazeConfig() {
		return CONFIG_LIST.get(0);
	}

	public static List<OpenTime> getOpenTimes() {
		return openTimes;
	}

	/**
	 * 根据等级获得怪物配置
	 */
	public static TreasureLevelConfig getMonsterByLevel(int level) {
		List<TreasureLevelConfig> list = new ArrayList<>();
		for (String key : TREASURE_LEVEL_CONFIG.keySet()) {
			List<Integer> arrays = StringUtils.delimiterString2IntList(key, Splitable.ATTRIBUTE_SPLIT);
			if (arrays.get(0) <= level && level <= arrays.get(1)) {
				list = TREASURE_LEVEL_CONFIG.get(key);
				break;
			}
		}
		;
		int proportion = 0;
		int num = RandomUtils.nextInt(0, 1000);
		for (TreasureLevelConfig config : list) {
			proportion += config.proportion;
			if (num < proportion) {
				return config;
			}
		}
		return list.get(0);
	}

	/**
	 * 获得怪物附加属性配置
	 */
	public static TreasureMonsterConfig getMonsterExpr(int monsterId) {
		return TREASURE_MONSTER_CONFIG.get(monsterId);
	}

	/**
	 * 获取兑换配置
	 */
	public static TreasureExchangeConfig getExchangeConfig(int exchangeId) {
		return TREASURE_EXCHANGE_CONFIG.get(exchangeId);
	}
	
	/**
	 * 获取兑换物品的id
	 */
	public static int getExchangeGoodsId(){
		for(TreasureExchangeConfig config:TREASURE_EXCHANGE_CONFIG.values()){
			return config.deductId;
		}
		return 0;
	}

	public static int getCostTicket(int level) {
		TreasureGlobalConfig config = getMazeConfig();
		return FormulaHelper.executeCeilInt(config.costTicket, level);
	}

	public static int getmaxCount() {
		TreasureGlobalConfig config = getMazeConfig();
		return config.count;
	}

	public static int getExchangeNum(int level) {
		TreasureGlobalConfig config = getMazeConfig();
		return FormulaHelper.executeCeilInt(config.getReward().num, level);
	}

	/**
	 * 获取保底步数
	 * @return
	 */
	public static int getleastStep() {
		TreasureGlobalConfig config = getMazeConfig();
		return config.getLeastNum();
	}

}
