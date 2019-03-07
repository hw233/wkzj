package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.TraderConditionConfig;
import com.jtang.gameserver.dataconfig.model.TraderDiscountConfig;
import com.jtang.gameserver.dataconfig.model.TraderFlushConfig;
import com.jtang.gameserver.dataconfig.model.TraderGlobalConfig;
import com.jtang.gameserver.dataconfig.model.TraderLeastConfig;
import com.jtang.gameserver.dataconfig.model.TraderPoolConfig;
import com.jtang.gameserver.dataconfig.model.TraderRewardPoolConfig;

@Component
public class TraderService extends ServiceAdapter {

	/** 云游商人触发条件 */
	private static Map<Integer, TraderConditionConfig> CONDITION_MAP = new HashMap<>();
	/** 云游商人商品列表 */
	private static Map<Integer, TraderPoolConfig> POOL_MAP = new HashMap<>();
	/** 云游商人商品折扣 */
	private static Map<Integer, TraderDiscountConfig> DISCOUNT_MAP = new HashMap<>();
	/** 云游商人商品池 */
	private static Map<Integer, TraderRewardPoolConfig> REWARD_MAP = new HashMap<>();
	/** 云游商人规则全局配置 */
	private static TraderGlobalConfig GLOBAL = new TraderGlobalConfig();
	/** 云游商人刷新消耗点券配置 */
	private static Map<Integer,TraderFlushConfig> FLUSH_MAP = new HashMap<>();
	/** 云游商人保底配置 */
	private static Map<Integer,TraderLeastConfig> LEAST_MAP = new HashMap<>();
	
	private static int MAX_FLUSH_NUM = 0;
	
	@Override
	public void clear() {
		CONDITION_MAP.clear();
		POOL_MAP.clear();
		DISCOUNT_MAP.clear();
		REWARD_MAP.clear();
		GLOBAL = new TraderGlobalConfig();
		FLUSH_MAP.clear();
		LEAST_MAP.clear();
	}

	@Override
	public void initialize() {
		List<TraderConditionConfig> conditionList = dataConfig.listAll(this, TraderConditionConfig.class);
		for(TraderConditionConfig config : conditionList){
			CONDITION_MAP.put(config.id, config);
		}
		
		List<TraderPoolConfig> poolList = dataConfig.listAll(this, TraderPoolConfig.class);
		for(TraderPoolConfig config : poolList){
			POOL_MAP.put(config.id, config);
		}
		
		List<TraderRewardPoolConfig> rewardList = dataConfig.listAll(this, TraderRewardPoolConfig.class);
		for(TraderRewardPoolConfig config : rewardList){
			REWARD_MAP.put(config.id, config);
		}
		
		List<TraderDiscountConfig> discountList = dataConfig.listAll(this, TraderDiscountConfig.class);
		for(TraderDiscountConfig config : discountList){
			DISCOUNT_MAP.put(config.id, config);
		}
		
		List<TraderGlobalConfig> globalList=  dataConfig.listAll(this, TraderGlobalConfig.class);
		GLOBAL = globalList.get(0);
		
		List<TraderFlushConfig> flushList = dataConfig.listAll(this, TraderFlushConfig.class);
		for(TraderFlushConfig config : flushList){
			FLUSH_MAP.put(config.flushNum, config);
			if(MAX_FLUSH_NUM < config.flushNum){
				MAX_FLUSH_NUM = config.flushNum;
			}
		}
		
		List<TraderLeastConfig> leastList = dataConfig.listAll(this, TraderLeastConfig.class);
		for (TraderLeastConfig config : leastList) {
			LEAST_MAP.put(config.id, config);
		}
	}
	
	public static TraderGlobalConfig getGlobalConfig(){
		return GLOBAL;
	}
	
	/**
	 * 是否可以刷新商品列表
	 * @param flushTime
	 * @param now
	 * @return
	 */
	public static boolean isFlush(int flushTime,int now){
		List<Integer> flushList = getFlushTime();
		for(Integer time:flushList){
			if(flushTime < time && time < now){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取触发条件配置
	 * @param id
	 * @return
	 */
	public static TraderConditionConfig getConditionConfig(int id){
		return CONDITION_MAP.get(id);
	}
	
	/**
	 * 获取打折配置
	 */
	public static TraderDiscountConfig getDiscountConfig(int id){
		return DISCOUNT_MAP.get(id);
	}
	
	/**
	 * 获取商品配置
	 */
	public static TraderRewardPoolConfig getRewardConfig(int id){
		return REWARD_MAP.get(id);
	}
	
	/**
	 * 获取触发条件列表
	 * @return
	 */
	public static List<TraderConditionConfig> randomConditionConfig(){
		TraderGlobalConfig globalConfig = getGlobalConfig();
		List<TraderConditionConfig> conditionList = new ArrayList<>();
		List<TraderConditionConfig> noMustList = new ArrayList<>();//没有必出条件的列表
		for(TraderConditionConfig config:CONDITION_MAP.values()){//先取出必出的条件
			if(conditionList.size() >= globalConfig.conditionNum){//满足配置数量,直接返回
				return conditionList;
			}
			if(config.must == 1){//必出添加到列表中
				conditionList.add(config);
			}else{//非必出添加到另外的列表进行随机
				noMustList.add(config);
			}
		}
		int num = globalConfig.conditionNum - conditionList.size();
		for (int i = 0; i < num; i++) {//列表中条件的数量未满足配置需要的数量,进行随机按几率抽取
			int index = RandomUtils.nextInt(0, noMustList.size() - 1);
			TraderConditionConfig config = noMustList.get(index);
			if(conditionList.contains(config)){
				i--;
				continue;
			}else{
				conditionList.add(config);
			}
		}
		return conditionList;
	}
	
	/**
	 * 获取本次刷新需要的点券
	 */
	public static int getFlushCostTicket(int flushNum){
		if(FLUSH_MAP.containsKey(flushNum)){
			return FLUSH_MAP.get(flushNum).getCostTicket(flushNum);
		}
		if(flushNum >= MAX_FLUSH_NUM){
			return FLUSH_MAP.get(-1).getCostTicket(flushNum);
		}
		return 0;
	}
	
	/**
	 * 随机抽取一个类型列表
	 * @return
	 */
	private static TraderPoolConfig randomPoolConfig(){
		Map<Integer,Integer> map = new HashMap<>();
		for (TraderPoolConfig config:POOL_MAP.values()) {
			map.put(config.id, config.rate);
		}
		Integer index = RandomUtils.randomHit(1000, map);
		return POOL_MAP.get(index);
	}
	
	/**
	 * 获取随机商品列表
	 * @param level
	 * @param vipLevel
	 * @param viewMap
	 * @return key:商品id value:打折id
	 */
	public static Map<Integer,Integer> randomReward(int level,int vipLevel,Map<Integer,Integer> viewMap,Map<Integer,Integer> leastMap){
		TraderPoolConfig poolConfig = randomPoolConfig();//随机出该次的奖励类型_数量列表
		List<TraderRewardPoolConfig> allReward = new ArrayList<>(getReward(level, vipLevel,viewMap));//随机出该等级、vip等级区间的所有奖励
		Map<Integer,Integer> rewardConfig = new HashMap<>();
		List<Integer> leastList = getLeast(leastMap);
		for(Entry<Integer,Integer> entry:poolConfig.typeMap.entrySet()){//根据类型随机
			rewardConfig.putAll(randomRewardByType(entry,allReward,leastList));
		}
		return rewardConfig;
	}
	
	/**
	 * 根据等级、vip等级、奖励出现次数获取奖励池
	 * @param level 掌教等级
	 * @param vipLevel vip等级
	 * @param viewMap 奖励id:出现次数
	 * @return
	 */
	private static List<TraderRewardPoolConfig> getReward(int level,int vipLevel,Map<Integer,Integer> viewMap){
		List<TraderRewardPoolConfig> rewardList = new ArrayList<>();
		for(TraderRewardPoolConfig config:REWARD_MAP.values()){//从所有奖励中遍历
			if(config.inLevel(level) && config.inVipLevel(vipLevel)){//取出等级相符合的
				if(viewMap.containsKey(config.id) && viewMap.get(config.id) < config.viewNum){//如果奖励出现次数还未达到配置上限,加入奖励池
					rewardList.add(config);
				}else if(viewMap.containsKey(config.id) == false){//如果奖励还未出现过也加入奖励池
					rewardList.add(config);
				}
			}
		}
		return rewardList;
	}
	
	/**
	 * 随机某个类型的奖励列表
	 * @param entry 类型_数量
	 * @param allReward 奖励池
	 * @return
	 */
	private static Map<Integer,Integer> randomRewardByType(Entry<Integer,Integer> entry,List<TraderRewardPoolConfig> allReward,List<Integer> leastList){
		Map<Integer,Integer> rewardMap = new HashMap<>();
		Map<Integer,Integer> indexMap = new HashMap<>();
		List<TraderRewardPoolConfig> removeList = new ArrayList<>();
		for (int i = 0; i < entry.getValue(); i++) {//该类型的奖励出几个就循环几次
			int rate = 0;
			Map<Integer,Integer> map = new HashMap<>();//记录id对应的几率,做随机
			for(TraderRewardPoolConfig config : allReward){//通过类型筛选出一部分奖励
				if(config.itemType == entry.getKey()){
					if(config.isMust == 1){//将必出的奖励放入列表
						rewardMap.put(config.id,0);
						removeList.add(config);
					}else{//不是必出的奖励则进行概率随机
						map.put(config.id, config.rate);
						rate += config.rate;
					}
				}
			}
			allReward.removeAll(removeList);//移除必出列表
			if(rewardMap.size() >= entry.getValue()){//当数量已经满了不再进行随机
				return rewardMap;
			}
			if(leastList.isEmpty() == false){//需要保底
				for(Integer leastId : new ArrayList<>(leastList)){
					TraderLeastConfig config = LEAST_MAP.get(leastId);
					Map<Integer,Integer> leastMap = config.getReward();
					for(TraderRewardPoolConfig rewardConfig : allReward){
						if(leastMap.containsKey(rewardConfig.id)){//保底
							rewardMap.put(rewardConfig.id,leastMap.get(rewardConfig.id));
							removeList.add(rewardConfig);
							leastList.remove(leastId);
						}
					}
					allReward.removeAll(removeList);
				}
			}
			if(rewardMap.size() >= entry.getValue()){//当数量已经满了不再进行随机
				return rewardMap;
			}
			if(rate == 0){
				return rewardMap;
			}
			Integer index = RandomUtils.randomHit(rate, map);
			if(indexMap.containsKey(index)){//判断是否超过同id出现次数限制
				for(;;){//TODO 这样是不是有点问题
					if(indexMap.get(index) >= getGlobalConfig().rewardRule){//同类型同id物品出现次数超过配置,从所有列表中删除该奖励id,重新随机
						allReward.remove(index);//移除已限制的
						index = RandomUtils.randomHit(rate, map);//重新随机
					}else{
						break;
					}
				}
				indexMap.put(index, indexMap.get(index) + 1);
			}else{
				indexMap.put(index, 1);
			}
			for(TraderRewardPoolConfig config : allReward){//将随机到的奖励添加到奖励列表
				if(config.id == index){
					rewardMap.put(config.id,0);
					removeList.add(config);
				}
			}
			allReward.remove(removeList);
			if(rewardMap.size() >= entry.getValue()){//当数量已经满了不再进行随机
				return rewardMap;
			}
		}
		return rewardMap;
	}
	
	/**
	 * 随机获取本次打折
	 */
	public static List<TraderDiscountConfig> randomDiscount(){
		int maxHit = 0;
		List<TraderDiscountConfig> list = new ArrayList<>();
		for(TraderDiscountConfig config : DISCOUNT_MAP.values()){
			if(getGlobalConfig().discountNum <= maxHit){
				return list;
			}
			boolean isHit = RandomUtils.is1000Hit(config.rate);
			if(isHit){
				maxHit ++;
				list.add(config);
			}
		}
		return list;
	}
	
	/**
	 * 获取一次打折
	 * @return
	 */
	public static TraderDiscountConfig getDiscount(){
		Map<Integer,Integer> map = new HashMap<>();
		List<TraderDiscountConfig> list = new ArrayList<>();
		int i = 0;
		for(TraderDiscountConfig config:DISCOUNT_MAP.values()){
			if(config.rate > 0){
				map.put(i, config.rate);
				list.add(config);
				i++;
			}
		}
		Integer index = RandomUtils.randomHit(1000, map);
		return list.get(index);
	}
	
	/**
	 * 获取保底
	 * @return
	 */
	public static List<Integer> getLeast(Map<Integer,Integer> leastMap){
		List<Integer> list = new ArrayList<>();
		for(Entry<Integer,Integer> entry : leastMap.entrySet()){
			TraderLeastConfig config = LEAST_MAP.get(entry.getKey());
			if(config.flushNum <= entry.getValue()){
				list.add(config.id);
			}
		}
		return list;
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

	public static TraderRewardPoolConfig getPoolRewardConfig(Integer key) {
		return REWARD_MAP.get(key);
	}

	/**
	 * 初始化保底
	 * @return
	 */
	public static Map<Integer, Integer> initLeast() {
		Map<Integer,Integer> map = new HashMap<>();
		for(Integer key:LEAST_MAP.keySet()){
			map.put(key, 0);
		}
		return map;
	}
	
	/**
	 * 通过itemId获取奖励配置
	 * @param vipLevel 
	 * @param level 
	 */
	public static List<TraderRewardPoolConfig> getRewardConfigByItemId(int itemId, int level, int vipLevel){
		List<TraderRewardPoolConfig> list = new ArrayList<>();
		for(TraderRewardPoolConfig config : REWARD_MAP.values()){
			if(config.itemId == itemId && config.inLevel(level) && config.inVipLevel(vipLevel)){
				list.add(config);
			}
		}
		return list;
	}
	
}
