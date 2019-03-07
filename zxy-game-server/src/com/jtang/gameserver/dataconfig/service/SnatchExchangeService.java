package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.SnatchExchangeConfig;

/**
 * 抢夺兑换配置服务类
 * @author 0x737263
 *
 */
@Component
public class SnatchExchangeService extends ServiceAdapter {
	
	private static Map<Integer,SnatchExchangeConfig> SNATCH_MAP = new HashMap<>();
	
//	private static SnatchExchangeGlobalConfig GLOBAL_CONFIG;
	
	@Override
	public void clear() {
		SNATCH_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<SnatchExchangeConfig> list = dataConfig.listAll(this, SnatchExchangeConfig.class);
		for (SnatchExchangeConfig config : list) {
//			if(SNATCH_MAP.containsKey(config.rewardType)){
//				SNATCH_MAP.get(config.rewardType).put(config.exchangeId, config);
//			}else{
//				Map<Integer,SnatchExchangeConfig> map = new HashMap<>();
//				map.put(config.exchangeId, config);
//				SNATCH_MAP.put(config.rewardType, map);
//			}
			SNATCH_MAP.put(config.exchangeId, config);
		}
//		GLOBAL_CONFIG = dataConfig.listAll(this, SnatchExchangeGlobalConfig.class).get(0);
	}
	
//	public static SnatchExchangeGlobalConfig getGlobalConfig(){
//		return GLOBAL_CONFIG;
//	}
	
	public static SnatchExchangeConfig getReward(int exchangeId){
		return SNATCH_MAP.get(exchangeId);
	}
	
//	public static Map<Integer,SnatchExchangeConfig> getReward(){
//		Map<Integer,SnatchExchangeConfig> exchangeMap = new HashMap<>();
//		Map<Integer,Integer> goodsPool = GLOBAL_CONFIG.goodsPoolMap;//奖励列表组成
//		for(Integer key:goodsPool.keySet()){//按类型遍历
//			int propertion = 0;
//			Map<Integer,SnatchExchangeConfig> typeMap = new HashMap<>(SNATCH_MAP.get(key));//new一个map,每次随机出一个减掉几率再随机
//			int mastRewardIndex = 0;
//			for (int i = 0; i < goodsPool.get(key); i++) {//每个类型有几个奖励
//				if(typeMap.containsKey(mastRewardIndex)){//已经有必出奖励,移除必出奖励
//					typeMap.remove(mastRewardIndex);
//					mastRewardIndex = 0;
//				}
//				Map<Integer,Integer> map = new HashMap<>();
//				for(Integer exchangeId:typeMap.keySet()){
//					if(typeMap.get(exchangeId).isMastReward()){//必出物品
//						exchangeMap.put(exchangeId, typeMap.get(exchangeId));
//						mastRewardIndex = exchangeId;
//						propertion += typeMap.get(exchangeId).proportion;//扣除必出物品的概率
//						continue;
//					}
//					map.put(exchangeId, typeMap.get(exchangeId).proportion);
//				}
//				if(mastRewardIndex != 0){
//					continue;
//				}
//				Integer index = RandomUtils.randomHit(1000 - propertion, map);
//				if(index == null){
//					continue;
//				}
//				exchangeMap.put(index, typeMap.get(index));
//				propertion += typeMap.get(index).proportion;
//				typeMap.remove(index);
//			}
//		}
//		return exchangeMap;
//	}
	
//	public static boolean isFlush(int flushTime,int now){
//		List<Integer> flushList = getFlushTime();
//		for(Integer time:flushList){
//			if(flushTime < time && time < now){
//				return true;
//			}
//		}
//		return false;
//	}
	
//	private static List<Integer> getFlushTime(){
//		List<Date> date = getGlobalConfig().getFlushTime();
//		List<Integer> flushTime = new ArrayList<>();
//		for(Date d : date){
//			Calendar c = Calendar.getInstance();
//			c.setTime(d);
//			Long time = c.getTimeInMillis() / 1000;
//			flushTime.add(time.intValue());
//		}
//		return flushTime;
//	}
	
}
