package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.TrialBattleConfig;
import com.jtang.gameserver.dataconfig.model.TrialCaveUpgradeConfig;

/**
 * 试炼洞服务类
 * @author 0x737263
 *
 */
@Component
public class TrialService extends ServiceAdapter {
	
	/**
	 * 试炼洞战场配置
	 */
	static Map<Integer, TrialBattleConfig> BATTLE_CONFIG_MAP = new HashMap<Integer, TrialBattleConfig>();
	
	/**
	 * 试炼洞室升级配置
	 */
	static Map<Integer, TrialCaveUpgradeConfig> TRIAL_CAVE_UPGRADE_CONFIG_MAP = new HashMap<Integer, TrialCaveUpgradeConfig>();
	
	/**
	 * 洞室最大等级
	 */
	private static int maxLevel = 0;
	
	@Override
	public void clear() {
		BATTLE_CONFIG_MAP.clear();
		TRIAL_CAVE_UPGRADE_CONFIG_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<TrialBattleConfig> battleList = dataConfig.listAll(this, TrialBattleConfig.class);
		
		//将所有的战场配置缓存
		for (TrialBattleConfig conf : battleList) {
			BATTLE_CONFIG_MAP.put(conf.getBattleId(), conf);
		}
		
		//试炼洞升级配置
		List<TrialCaveUpgradeConfig> caveLvConfigList = dataConfig.listAll(this, TrialCaveUpgradeConfig.class);
		for (TrialCaveUpgradeConfig conf : caveLvConfigList) {
			TRIAL_CAVE_UPGRADE_CONFIG_MAP.put(conf.getLevel(), conf);
			
			if(maxLevel < conf.getLevel()) {
				maxLevel = conf.getLevel();
			}
		}
	}
	
	public static TrialBattleConfig getBattle(int battleId) {
		return BATTLE_CONFIG_MAP.get(battleId);
	}
	
	public static TrialCaveUpgradeConfig getUpgradeConfig(int currentLevel) {
		return TRIAL_CAVE_UPGRADE_CONFIG_MAP.get(currentLevel);
	}
	
	/**
	 * 洞室最大的等级
	 * @return
	 */
	public static int maxLevel() {
		return maxLevel;
	}

}
