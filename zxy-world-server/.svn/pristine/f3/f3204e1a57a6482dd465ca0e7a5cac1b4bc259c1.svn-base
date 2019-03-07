package com.jtang.worldserver.module.crossbattle.helper;

import java.util.Map;

import com.jtang.worldserver.dataconfig.model.CrossBattleRankConfig;
import com.jtang.worldserver.dataconfig.service.CrossBattleRankService;

public class CrossBattleHelper {
	/**
	 * 击杀玩家获得的额外伤害
	 * @return
	 */
	public static int killActorRewardHurt(int rank, int hpMax) {
		CrossBattleRankConfig cfg = CrossBattleRankService.get(rank);
		if (cfg == null) {
			return 0;
		}
		int base = hpMax;
		Double d = cfg.getKillPercent() / 100.0d;
		Double value = base * d;
		return value.intValue();
	}
	
	/**
	 * 设置排名产生的额外伤害
	 * @return
	 */
	public static int powerRankRewardHurt(int rank, int baseHurt) {
		CrossBattleRankConfig cfg = CrossBattleRankService.get(rank);
		if (cfg == null) {
			return 0;
		}
		int base = baseHurt;
		Double d = cfg.getHurtPercent() / 100.0d;
		Double value = base * d;
		return value.intValue();
	}
	
	/**
	 * 计算基本伤害
	 * @param baseHurt
	 * @return
	 */
	public static int getBaseTotalHurt(Map<Integer, Integer> baseHurt) {
		int result = 0;
		for (int value : baseHurt.values()) {
			result += value;
		}
		return result;
	}
	
}
