package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.BlackShopConfig;
import com.jtang.gameserver.dataconfig.model.BlackShopRewardConfig;

/**
 * 抢夺兑换配置服务类
 * @author 0x737263
 *
 */
@Component
public class BlackShopService extends ServiceAdapter {
	
	private static Map<Integer,Map<Integer,BlackShopRewardConfig>> REWARD_MAP = new HashMap<>();
	
	private static BlackShopConfig GLOBAL_CONFIG;
	
	@Override
	public void clear() {
		REWARD_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<BlackShopRewardConfig> list = dataConfig.listAll(this, BlackShopRewardConfig.class);
		for (BlackShopRewardConfig config : list) {
			if(REWARD_MAP.containsKey(config.rewardType)){
				REWARD_MAP.get(config.rewardType).put(config.exchangeId, config);
			}else{
				Map<Integer,BlackShopRewardConfig> map = new HashMap<>();
				map.put(config.exchangeId, config);
				REWARD_MAP.put(config.rewardType, map);
			}
		}
		GLOBAL_CONFIG = dataConfig.listAll(this, BlackShopConfig.class).get(0);
	}
	
	public static BlackShopConfig getGlobalConfig(){
		return GLOBAL_CONFIG;
	}
	
	public static Map<Integer,BlackShopRewardConfig> getReward(){
		Map<Integer,BlackShopRewardConfig> exchangeMap = new HashMap<>();
		Map<Integer,Integer> goodsPool = GLOBAL_CONFIG.goodsPoolMap;//奖励列表组成
		for(Integer key:goodsPool.keySet()){//按类型遍历
			int propertion = 0;
			Map<Integer,BlackShopRewardConfig> typeMap = new HashMap<>(REWARD_MAP.get(key));//new一个map,每次随机出一个减掉几率再随机
			int mastRewardIndex = 0;
			for (int i = 0; i < goodsPool.get(key); i++) {//每个类型有几个奖励
				if(typeMap.containsKey(mastRewardIndex)){//已经有必出奖励,移除必出奖励
					propertion += typeMap.get(mastRewardIndex).proportion;
					typeMap.remove(mastRewardIndex);
					mastRewardIndex = 0;
				}
				Map<Integer,Integer> map = new HashMap<>();
				for(Integer exchangeId:typeMap.keySet()){
					if(typeMap.get(exchangeId).isMastReward()){//必出物品
						exchangeMap.put(exchangeId, typeMap.get(exchangeId));
						mastRewardIndex = exchangeId;
						continue;
					}
					map.put(exchangeId, typeMap.get(exchangeId).proportion);
				}
				if(mastRewardIndex != 0){
					continue;
				}
				Integer index = RandomUtils.randomHit(1000 - propertion, map);
				if(index == null){
					continue;
				}
				exchangeMap.put(index, typeMap.get(index));
				propertion += typeMap.get(index).proportion;
				typeMap.remove(index);
			}
		}
		return exchangeMap;
	}
	
	public static boolean isFlush(int flushTime,int now){
		List<Integer> flushList = getFlushTime();
		for(Integer time:flushList){
			if(flushTime < time && time < now){
				return true;
			}
		}
		return false;
	}
	
	private static List<Integer> getFlushTime(){
		List<Date> date = getGlobalConfig().getFlushTime();
		List<Integer> flushTime = new ArrayList<>();
		for(Date d : date){
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			Long time = c.getTimeInMillis() / 1000;
			flushTime.add(time.intValue());
		}
		return flushTime;
	}
}
