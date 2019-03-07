package com.jtang.worldserver.dataconfig.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.worldserver.dataconfig.model.CrossBattleEndAwardConfig;

@Component
public class CrossBattleEndAwardService extends ServiceAdapter {
	
//	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	/**
	 * key：awardId
	 */
	private static List<CrossBattleEndAwardConfig> rewardList = new ArrayList<>();

	private static int maxRank;
	@Override
	public void clear() {
		rewardList.clear();
	}

	@Override
	public void initialize() {
		List<CrossBattleEndAwardConfig> list = dataConfig.listAll(this, CrossBattleEndAwardConfig.class);
		
		for (CrossBattleEndAwardConfig crossBattleEndAwardConfig : list) {
			rewardList.add(crossBattleEndAwardConfig);
			maxRank = maxRank > crossBattleEndAwardConfig.getAllowRank() ? maxRank : crossBattleEndAwardConfig.getAllowRank();
		}
	}
	
	public static String getCrossBattleEndReward(int rank){
		for (CrossBattleEndAwardConfig cfg : rewardList) {
			if (rank == cfg.getAllowRank()) {
				return cfg.getReward();
			}
		}
		return "";
	}
	
	public static int getMaxRank() {
		return maxRank;
	}
	

}
