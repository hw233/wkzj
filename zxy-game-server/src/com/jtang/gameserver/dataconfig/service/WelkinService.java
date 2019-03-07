package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.gameserver.dataconfig.model.WelkinConfig;
import com.jtang.gameserver.dataconfig.model.WelkinGlobalConfig;
import com.jtang.gameserver.dataconfig.model.WelkinRankConfig;
import com.jtang.gameserver.dataconfig.model.WelkinRewardConfig;

@Component
public class WelkinService extends ServiceAdapter {

	private static Map<Integer,List<WelkinRewardConfig>> WELKIN_REWARD_MAP = new HashMap<>();
	
	private static List<WelkinGlobalConfig> WELKIN_GLOBAL = new ArrayList<>();
	
	private static Map<Integer,WelkinConfig> WELKIN_MAP = new HashMap<>();
	
	private static Map<Integer,WelkinRankConfig> WELKIN_RANK_MAP = new HashMap<>();
	
	@Override
	public void clear() {
		WELKIN_REWARD_MAP.clear();
		WELKIN_GLOBAL.clear();
		WELKIN_MAP.clear();
		WELKIN_RANK_MAP.clear();
	}

	@Override
	public void initialize() {
		
		List<WelkinRewardConfig> rewardList = dataConfig.listAll(this, WelkinRewardConfig.class);
		for (WelkinRewardConfig config : rewardList) {
			if(WELKIN_REWARD_MAP.containsKey(config.id)){
				WELKIN_REWARD_MAP.get(config.id).add(config);
			}else{
				List<WelkinRewardConfig> list = new ArrayList<WelkinRewardConfig>();
				list.add(config);
				WELKIN_REWARD_MAP.put(config.id, list);
			}
		}
		
		List<WelkinGlobalConfig> globalList = dataConfig.listAll(this, WelkinGlobalConfig.class);
		WELKIN_GLOBAL.addAll(globalList);
		
		List<WelkinConfig> welkinList = dataConfig.listAll(this, WelkinConfig.class);
		for(WelkinConfig config : welkinList){
			WELKIN_MAP.put(config.id, config);
		}
		
		List<WelkinRankConfig> rankList = dataConfig.listAll(this, WelkinRankConfig.class);
		for (WelkinRankConfig config : rankList) {
			WELKIN_RANK_MAP.put(config.rank, config);
		}
	}
	
	/**
	 * 随机获得一个奖励
	 * 根据所在位置
	 * @param place 位置
	 * @param isGoodReward 是否获得保底
	 * @return
	 */
	public static int getReward(int place,boolean isGoodReward){
		List<WelkinRewardConfig> rewardConfig = WELKIN_REWARD_MAP.get(place);
		Map<Integer, Integer> map = new HashMap<>();
		for(int i = 0;i < rewardConfig.size() ; i++){
			WelkinRewardConfig config = rewardConfig.get(i);
			if(config.breakEven == 1 && isGoodReward){//获得保底奖励
				return i;
			}
			map.put(i, config.propertion);
		}
		Integer index = RandomUtils.randomHit(1000, map);
		if(index == null){
			return 0;
		}
		return index;
	}
	
	/**
	 * 获取全局配置
	 */
	public static WelkinGlobalConfig getWelKinGlobalConfig(){
		return WELKIN_GLOBAL.get(0);
	}
	
	/**
	 * 获取排名配置
	 */
	public static List<RewardObject> getWelkinRankConfig(int runk ,int useNum){
		WelkinRankConfig rankConfig = WELKIN_RANK_MAP.get(runk);
		if(rankConfig == null){
			return null;
		}else{
			if(rankConfig.useNum > useNum){
				return null;
			}
		}
		List<RewardObject> rewardList = new ArrayList<>();
		RewardType type = RewardType.getType(rankConfig.type);
		RewardObject rewardObject = new RewardObject(type,rankConfig.id,rankConfig.num);
		rewardList.add(rewardObject);
		return rewardList;
	}

	/**
	 * 获取当前使用次数获得配置
	 */
	public static WelkinConfig getWelkinConfig(int num){
		for(WelkinConfig config : WELKIN_MAP.values()){
			if(num <= config.intervalEnd){
				return config;
			}
		}
		return WELKIN_MAP.get(WELKIN_MAP.size());
	}
	
	/**
	 * 获取当前位置所有奖励
	 * @param place 当前所在位置
	 */
	public static List<RewardObject> getAllReward(int place,int level){
		List<WelkinRewardConfig> rewardConfig = WELKIN_REWARD_MAP.get(place);
		List<RewardObject> list = new ArrayList<>();
		for(WelkinRewardConfig reward : rewardConfig){
			RewardObject rewardObject = parseToRewardObject(level,reward);
			list.add(rewardObject);
		}
		return list;
	}
	
	/**
	 * 获取当前位置所有奖励
	 * @param level
	 * @param reward
	 * @return
	 */
	public static List<WelkinRewardConfig> getAllRewardConfig(int place){
		return WELKIN_REWARD_MAP.get(place);
	}
	
	public static RewardObject parseToRewardObject(int level,WelkinRewardConfig reward){
		RewardObject rewardObject = new RewardObject();
		rewardObject.rewardType = RewardType.getType(reward.type);
		rewardObject.id = reward.itemId;
		rewardObject.num = FormulaHelper.executeCeilInt(reward.num, level);
		return rewardObject;
	}

	/**
	 * 根据次数是否出保底奖励
	 * @param useCount 已使用次数
	 * @param count 本次抽奖次数
	 * @return
	 */
	public static boolean isGoodReward(int useCount, int count) {
		WelkinConfig welkinConfig = getWelkinConfig(useCount + count);
		if(welkinConfig.endCount == -1){
			return false;
		}else{
			return welkinConfig.endCount <= useCount + count && welkinConfig.startCount > useCount;
		}
	}

	/**
	 * 获得展示位置配置
	 * @return
	 */
	public static String getPlace() {
		StringBuffer sb = new StringBuffer();
		for(WelkinConfig config:WELKIN_MAP.values()){
			sb.append(config.intervalEnd);
			sb.append(Splitable.ATTRIBUTE_SPLIT);
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
}
