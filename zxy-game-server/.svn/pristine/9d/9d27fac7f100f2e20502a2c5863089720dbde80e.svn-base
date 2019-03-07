package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.HeroUpgradeConfig;

/**
 * 仙人升级服务类
 * @author 0x737263
 *
 */
@Component
public class HeroUpgradeService extends ServiceAdapter {

	/**
	 * key: level_star value:HeroUpgradeConfig
	 */
	private static Map<String, HeroUpgradeConfig> HERO_UPGRADE_CONFIG_MAPS = new HashMap<String, HeroUpgradeConfig>();

	private static List<HeroUpgradeConfig> HERO_UPGRADE_LIST = new ArrayList<>();
	
	private static Map<Integer, Integer> HERO_UPGRADE_MAX_LEVEL = new HashMap<Integer, Integer>();
	
	@Override
	public void clear() {
		HERO_UPGRADE_CONFIG_MAPS.clear();
		HERO_UPGRADE_LIST.clear();
		HERO_UPGRADE_MAX_LEVEL.clear();
	}
	
	@Override
	public void initialize() {
		HERO_UPGRADE_LIST.addAll(dataConfig.listAll(this, HeroUpgradeConfig.class));
		for (HeroUpgradeConfig upgrade : HERO_UPGRADE_LIST) {
			HERO_UPGRADE_CONFIG_MAPS.put(getKey(upgrade.getLevel(), upgrade.getStar()), upgrade);
			if(HERO_UPGRADE_MAX_LEVEL.containsKey(upgrade.getStar()))
				HERO_UPGRADE_MAX_LEVEL.put(upgrade.getStar(), HERO_UPGRADE_MAX_LEVEL.get(upgrade.getStar()) + 1);
			else
				HERO_UPGRADE_MAX_LEVEL.put(upgrade.getStar(), 1);
		}
	}
	
	public static int getMaxLevel(int star){
		return HERO_UPGRADE_MAX_LEVEL.get(star);
	}
	/**
	 * 生成maps的key
	 * @param level
	 * @param star
	 * @return
	 */
	private static String getKey(int level, int star) {
		return String.valueOf(level) + "_" + String.valueOf(star);
	}
	
	/**
	 * 获取仙人升级配置
	 * @param level
	 * @param star
	 * @return
	 */
	public static HeroUpgradeConfig get(int level, int star) {
		return HERO_UPGRADE_CONFIG_MAPS.get(getKey(level, star));
	}
	
	/**
	 * 根据仙人当前等级和星级以及总经验计算仙人可升的级数
	 * @param level				仙人当前等级(没升级前)
	 * @param star				仙人星级
	 * @param totalExp			预计的总经验(当前经验+新增加的经验)
	 * @return 仙人可提升的级数
	 */
	public static int getAbleUpgrades(int level, int star, int totalExp) {
		int maxLevel = getMaxLevel(star);
		if (maxLevel < 1 || level >= maxLevel) {
			return 0;
		}
		
		int ableUpgrades = 0;
		while (level + ableUpgrades < maxLevel) {
			HeroUpgradeConfig upgradeConfig = get(level + ableUpgrades, star);
			if(totalExp < upgradeConfig.getNeedExp()) {
				break;
			}
			totalExp -= upgradeConfig.getNeedExp();
			ableUpgrades++;
		}
		return ableUpgrades;
	}
	
//	/**
//	 * 获取升级后的剩余经验
//	 * @param level			仙人等级
//	 * @param star			星级
//	 * @param totalExp		总计经验
//	 * @return	返回 剩余经验
//	 */
//	@Deprecated
//	public static int getResidueExp(int level, int star, int totalExp) {
//		int maxLevel = getMaxLevel(star);
//		if (level > maxLevel) {
//			return 0;
//		}
//		HeroUpgradeConfig upgradeConfig = get(level, star);
//		int exp = totalExp - upgradeConfig.getNeedExp();
//		return exp > 0 ? exp : 0;
//	}
	
	/**
	 * 获取提升到当前等级需要的经验
	 * @return
	 */
	public static int getExp(int level, int star) {
		if(level > 1){
			int totalExp = 0;
			while(level > 1){
				level--;
				totalExp += get(level, star).getNeedExp();
			}
			return totalExp;
		}
		return 0;
	}
}
