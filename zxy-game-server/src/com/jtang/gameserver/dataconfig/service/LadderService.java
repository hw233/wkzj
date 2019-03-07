package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.LadderAttributeConfig;
import com.jtang.gameserver.dataconfig.model.LadderFightConfig;
import com.jtang.gameserver.dataconfig.model.LadderGlobalConfig;
import com.jtang.gameserver.dataconfig.model.LadderRankRewardConfig;

@Component
public class LadderService extends ServiceAdapter  {

	private static Map<Integer,List<LadderFightConfig>> LADDER_FIGHT_MAP = new HashMap<>();
	
	private static LadderGlobalConfig LADDER_GLOBAL = new LadderGlobalConfig();
	
	private static Map<String,LadderRankRewardConfig> RANK_REWARD_MAP = new HashMap<>();
	
	private static Map<Integer,LadderAttributeConfig> ATTRIBUTE_MAP = new HashMap<>();
	
	@Override
	public void clear() {
		LADDER_FIGHT_MAP.clear();
		LADDER_GLOBAL = new LadderGlobalConfig();
		RANK_REWARD_MAP.clear();
		ATTRIBUTE_MAP.clear();
	}

	@Override
	public void initialize() {
		List<LadderFightConfig> fightList = dataConfig.listAll(this, LadderFightConfig.class);
		for(LadderFightConfig config:fightList){
			if(LADDER_FIGHT_MAP.containsKey(config.fightType)){
				LADDER_FIGHT_MAP.get(config.fightType).add(config);
			}else{
				List<LadderFightConfig> configList = new ArrayList<>();
				configList.add(config);
				LADDER_FIGHT_MAP.put(config.fightType, configList);
			}
		}
		
		List<LadderGlobalConfig> globalList =  dataConfig.listAll(this, LadderGlobalConfig.class);
		for(LadderGlobalConfig config:globalList){
			LADDER_GLOBAL = config;
		}
		
		List<LadderRankRewardConfig> rankList = dataConfig.listAll(this, LadderRankRewardConfig.class);
		for(LadderRankRewardConfig config:rankList){
			RANK_REWARD_MAP.put(config.rank, config);
		}
		
		List<LadderAttributeConfig> attributeList = dataConfig.listAll(this, LadderAttributeConfig.class);
		for(LadderAttributeConfig config : attributeList){
			ATTRIBUTE_MAP.put(config.level, config);
		}
	}
	
	public static LadderGlobalConfig getGlobalConfig(){
		return LADDER_GLOBAL;
	}
	
	public static LadderFightConfig getFightConfig(int fightType){
		List<LadderFightConfig> list = LADDER_FIGHT_MAP.get(fightType);
		Map<Integer,Integer> map = new HashMap<>();
		for (int i = 0; i < list.size(); i++) {
			LadderFightConfig config = list.get(i);
			map.put(i, config.rate);
		}
		Integer index = RandomUtils.randomHit(1000, map);
		if(index == null){
			return null;
		}
		return LADDER_FIGHT_MAP.get(fightType).get(index);
	}
	
	public static LadderRankRewardConfig getRankConfig(int rank){
		for(Entry<String,LadderRankRewardConfig> entry:RANK_REWARD_MAP.entrySet()){
			LadderRankRewardConfig rankConfig = entry.getValue();
			if(rankConfig.startRank <= rank && rank <= rankConfig.endRank){
				return rankConfig;
			}
		}
		return null;
	}
	
	public static LadderAttributeConfig getAttriBute(int level){
		return ATTRIBUTE_MAP.get(level);
	}

}
