package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.BableRankRewardConfig;

/**
 * 通天塔数据配置服务类
 * @author 0x737263
 *
 */
@Component
public class BableRankService extends ServiceAdapter {
	
private static final Logger LOGGER = LoggerFactory.getLogger(BableService.class);
	
	
	/**
	 * key:bableId BableRankRewardConfig
	 */
	private static Map<Integer, List<BableRankRewardConfig>> BABLE_RANK_MAPS = new HashMap<>();

	@Override
	public void clear() {
		BABLE_RANK_MAPS.clear();
	}

	@Override
	public void initialize() {
		List<BableRankRewardConfig> list = dataConfig.listAll(this, BableRankRewardConfig.class);
		for (BableRankRewardConfig bableRankRewardConfig : list) {
			List<BableRankRewardConfig> cfgs = null;
			if (BABLE_RANK_MAPS.containsKey(bableRankRewardConfig.bableType)) {
				cfgs = BABLE_RANK_MAPS.get(bableRankRewardConfig.bableType);
			} else {
				cfgs = new ArrayList<>();
				BABLE_RANK_MAPS.put(bableRankRewardConfig.bableType, cfgs);
			}
			cfgs.add(bableRankRewardConfig);
		}
	}
	
	public static BableRankRewardConfig get(int bableId, int rank) {
		if (BABLE_RANK_MAPS.containsKey(bableId) == false) {
			LOGGER.warn(String.format("不存在奖励配置, bableId[%s]", bableId));
			return null;
		}
		
		List<BableRankRewardConfig> cfgs = BABLE_RANK_MAPS.get(bableId);
		for (BableRankRewardConfig bableRankRewardConfig : cfgs) {
			if (rank == bableRankRewardConfig.rank) {
				return bableRankRewardConfig;
			}
		}
		return null;
	}
	

}
