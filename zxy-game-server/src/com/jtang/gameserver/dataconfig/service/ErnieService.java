package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.RandomRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.ErnieGlobalConfig;
import com.jtang.gameserver.dataconfig.model.ErnieRewardConfig;
import com.jtang.gameserver.dbproxy.entity.Ernie;
import com.jtang.gameserver.module.goods.constant.GoodsRule;

@Component
public class ErnieService extends ServiceAdapter {

	public static ErnieGlobalConfig ERNIE_GLOBAL_CONFIG = new ErnieGlobalConfig();

	public static Map<Integer, ErnieRewardConfig> ERNIE_REWARD_MAP = new HashMap<Integer, ErnieRewardConfig>();
	
	@Override
	public void clear() {
		ERNIE_GLOBAL_CONFIG = new ErnieGlobalConfig();
		ERNIE_REWARD_MAP.clear();
	}

	@Override
	public void initialize() {
		List<ErnieGlobalConfig> global = dataConfig.listAll(this, ErnieGlobalConfig.class);
		for (ErnieGlobalConfig ernieGlobalConfig : global) {
			ERNIE_GLOBAL_CONFIG = ernieGlobalConfig;
		}

		List<ErnieRewardConfig> ernieRewardConfigs = dataConfig.listAll(this, ErnieRewardConfig.class);
		for (ErnieRewardConfig ernieRewardConfig : ernieRewardConfigs) {
			ERNIE_REWARD_MAP.put(ernieRewardConfig.count, ernieRewardConfig);
		}
		
		Collections.unmodifiableMap(ERNIE_REWARD_MAP);
	}
	
	public static boolean isOpen() {
		int startTime = getStartTime();
		int endTime = getEndTime();
		if (DateUtils.isActiveTime(startTime, endTime)) {
			return true;
		}
		return false;
	}
	
	
	public static boolean isExchangeTime() {
		int startTime = getExchangeStartTime();
		int endTime = getExchangeEndTime();
		if (DateUtils.isActiveTime(startTime, endTime)) {
			return true;
		}
		return false;
	}
	
	public static int getCostTicketCount(int totalCount) {
		int maxCount = ERNIE_REWARD_MAP.size() - 1;
		int realCount = -1;
		if (totalCount > maxCount) {
			realCount = maxCount;
			return realCount;
		}
		int freeCount = ERNIE_GLOBAL_CONFIG.freeTimes;
		realCount = totalCount - freeCount;
		if (realCount < 0) {
			return 0;
		}
		if (realCount == 0) {
			return 1;
		}
		return realCount;
	}

	public static List<RandomRewardObject> getRewardListByCount(int count) {
//		int count = getCostTicketCount(totalCount);
		List<RandomRewardObject> list = new ArrayList<RandomRewardObject>();
		if (ERNIE_REWARD_MAP.isEmpty()) {
			return list;
		}
		if (ERNIE_REWARD_MAP.containsKey(count)) {
			return ERNIE_REWARD_MAP.get(count).goodsList;
		}
		return list;
	}
	
	public static int getCostByErnieCount(int totalCount) {
		int count = getCostTicketCount(totalCount);
		if (ERNIE_REWARD_MAP.isEmpty()) {
			return count;
		}
		if (ERNIE_REWARD_MAP.containsKey(count)) {
			return ERNIE_REWARD_MAP.get(count).costTicket;
		}
		return count;
	}
 
	public static List<RewardObject> getLeastRewardObjectByErnieCount(List<RewardObject> rewardObjects, Ernie ernie, int billNum) {
		int offsetCount = getCostTicketCount(ernie.ernieCount);
		
		List<RandomRewardObject> randomList = ErnieService.getRewardListByCount(offsetCount);
		int num = 3 - rewardObjects.size();
		Map<Integer,Integer> map = convertList2Map(randomList);
		for (int i = 0; i < num; i++) {
			Integer index = RandomUtils.randomHit(1000, map);
			if(index == null){
				return rewardObjects;
			}
			RandomRewardObject orgReward = randomList.get(index);
			if (containsRewardObject(rewardObjects, orgReward, billNum) == true) {
				i--;
				continue;
			}
			rewardObjects.add(orgReward);
		}
		boolean needLeast = true;
		List<Integer> idList = ERNIE_GLOBAL_CONFIG.leastEquipIDList;
		for (RewardObject rewardObject : rewardObjects) {
			if (idList.contains(rewardObject.id) == true) {
				needLeast = false;
				ernie.setLeastNum(0);
			}
		}
		
		if (needLeast == true) {
			int leastMin = ERNIE_GLOBAL_CONFIG.leastNum;
			int leastMax = ERNIE_GLOBAL_CONFIG.leastNum;
			if (leastGoods(ernie, leastMin, leastMax) == true) {
				Map<Integer,Integer> map1 = convertList2Map(ERNIE_GLOBAL_CONFIG.leastEquipList);
				Integer index = RandomUtils.randomHit(1000, map1);
				if(index == null){
					return rewardObjects;
				}
				ernie.setLeastNum(0);
				RandomRewardObject extReward = ERNIE_GLOBAL_CONFIG.leastEquipList.get(index);
				int removeIndex = RandomUtils.nextIntIndex(rewardObjects.size());
				rewardObjects.remove(removeIndex);
				rewardObjects.add(removeIndex, extReward);
			}
		}
		return rewardObjects;
	}
	
	/**
	 * 物品保底
	 * @param actorId
	 * @param goodsId
	 * @return
	 */
	public static boolean leastGoods(Ernie ernie, int leastMin, int leastMax) {
		if (leastMax < leastMin) {
			return false;
		}
		int attNum = ernie.ernieCount;
		if (attNum < leastMin) {
			return false;
		}
		
		//实际攻击次数
		int roundAttNum = ernie.getLeastNum();
		if (roundAttNum < leastMin) return false;
		leastMin = Math.max(leastMin, roundAttNum); 
		int leastNum = RandomUtils.nextInt(leastMin, leastMax);
		if (leastNum == 0) {
			return false;
		}
		
		if (roundAttNum % leastNum == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	private static <T> boolean containsRewardObject(List<? extends RewardObject> list, RewardObject reward, int nowBillNum) {
		if (reward.id == GoodsRule.GOODS_ID_BILL) {
			if (nowBillNum >= ERNIE_GLOBAL_CONFIG.maxBillNum) {
				return true;
			}
		}
		for (RewardObject elements : list) {
			if (elements.id == reward.id) {
				return true;
			}
		}
		return false;
	}
	
	private static Map<Integer, Integer> convertList2Map(List<? extends RandomRewardObject> list) {
		List<? extends RandomRewardObject> reward = list;
		if(reward.size() == 0){
			return null;
		}
		Map<Integer,Integer> map = new HashMap<>();
		for (int i = 0; i < reward.size(); i++) {
			map.put(i, reward.get(i).rate);
		}
		return map;
	}
	
	public static int getStartTime() {
		return (int)(ERNIE_GLOBAL_CONFIG.openDateTime.getTime() / 1000L);
	}

	public static int getEndTime() {
		return (int)(ERNIE_GLOBAL_CONFIG.closeDateTime.getTime() / 1000L);
	}
	
	
	public static int getExchangeStartTime() {
		return (int)(ERNIE_GLOBAL_CONFIG.exchangeStartTime.getTime() / 1000L);
	}
	
	public static int getExchangeEndTime() {
		return (int)(ERNIE_GLOBAL_CONFIG.exchangeEndTime.getTime() / 1000L);
	}
	
	public static List<RewardObject> getAllErnieGoodsList(){
		return ERNIE_GLOBAL_CONFIG.ernieGoodsList;
	}
	
}
