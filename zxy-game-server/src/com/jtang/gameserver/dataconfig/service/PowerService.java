package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.PowerBattleConfig;
import com.jtang.gameserver.dataconfig.model.PowerFlushConfig;
import com.jtang.gameserver.dataconfig.model.PowerGlobalConfig;
import com.jtang.gameserver.dataconfig.model.PowerRankRewardConfig;
import com.jtang.gameserver.dataconfig.model.PowerShopConfig;
import com.jtang.gameserver.dataconfig.model.PowerShopFlushConfig;
import com.jtang.gameserver.module.power.model.PowerShopVO;

@Component
public class PowerService extends ServiceAdapter {
	
	/** 获得新排行奖励 */
	private static Map<Integer,PowerRankRewardConfig> POWER_RANK_REWARD_MAP = new HashMap<>();
	/** 排行战斗配置 */
	private static Map<String,PowerBattleConfig> POWER_BATTLE_MAP = new HashMap<>();
	/** 排行全局配置 */
	private static PowerGlobalConfig GLOBAL_CONFIG = new PowerGlobalConfig();
	/** 排行榜消耗点券刷新配置 */
	private static Map<Integer,PowerFlushConfig> FLUSH_MAP = new HashMap<>();
	/** 商店商品配置 */
	private static Map<Integer,PowerShopConfig> POWER_SHOP_CONFIG = new HashMap<>();
	/** 商店刷新消耗点券配置 */
	private static Map<Integer,PowerShopFlushConfig> POWER_SHOP_FLUSH_CONFIG = new HashMap<>();
	
	private static int MAX_REWARD_RANK;
	
	private static int MAX_FLUSH_NUM = 0;
	
	private static int MAX_SHOP_FLUSH = 0;
	
	@Override
	public void clear() {
		POWER_RANK_REWARD_MAP.clear();
		POWER_BATTLE_MAP.clear();
		GLOBAL_CONFIG = new PowerGlobalConfig();
		MAX_REWARD_RANK = 0;
		FLUSH_MAP.clear();
		POWER_SHOP_CONFIG.clear();
		POWER_SHOP_FLUSH_CONFIG.clear();
	}

	@Override
	public void initialize() {
		List<PowerRankRewardConfig> rewardList = dataConfig.listAll(this, PowerRankRewardConfig.class);
		for (PowerRankRewardConfig config : rewardList) {
			POWER_RANK_REWARD_MAP.put(config.rank, config);
			if(config.rank > MAX_REWARD_RANK){
				MAX_REWARD_RANK = config.rank;
			}
		}
		
		List<PowerBattleConfig> battleConfig = dataConfig.listAll(this, PowerBattleConfig.class);
		for (PowerBattleConfig config : battleConfig) {
			POWER_BATTLE_MAP.put(parserRank(config.rankBegin, config.rankEnd), config);
		}
		
		List<PowerGlobalConfig> globalConfig = dataConfig.listAll(this, PowerGlobalConfig.class);
		GLOBAL_CONFIG = globalConfig.get(0);
		
		List<PowerFlushConfig> flushList = dataConfig.listAll(this, PowerFlushConfig.class);
		for(PowerFlushConfig config : flushList){
			FLUSH_MAP.put(config.flushNum, config);
			if(MAX_FLUSH_NUM < config.flushNum){
				MAX_FLUSH_NUM = config.flushNum;
			}
		}
		
		List<PowerShopConfig> shopList = dataConfig.listAll(this, PowerShopConfig.class);
		for (PowerShopConfig config : shopList) {
			POWER_SHOP_CONFIG.put(config.id, config);
		}
		
		List<PowerShopFlushConfig> shopFlushList = dataConfig.listAll(this, PowerShopFlushConfig.class);
		for (PowerShopFlushConfig config : shopFlushList) {
			POWER_SHOP_FLUSH_CONFIG.put(config.flushNum, config);
			if(MAX_SHOP_FLUSH < config.flushNum){
				MAX_SHOP_FLUSH = config.flushNum;
			}
		}
	}
	
	public static PowerGlobalConfig getGlobalConfig(){
		return GLOBAL_CONFIG;
	}
	
	public static PowerBattleConfig getBattleConfig(int rank){
		for(Entry<String,PowerBattleConfig> entry:POWER_BATTLE_MAP.entrySet()){
			if(isRank(rank,entry.getKey())){
				return entry.getValue();
			}
		}
		return null;
	}
	
	public static List<RewardObject> getRankReward(int rank,int oldRank){
		Map<Integer,RewardObject> map = new HashMap<>();
		for(PowerRankRewardConfig config : POWER_RANK_REWARD_MAP.values()){
			if(config.rank >= rank && config.rank < oldRank){
				for (RewardObject rewardObject : config.getReward()) {
					RewardObject obj = new RewardObject(rewardObject.rewardType,rewardObject.id,rewardObject.num);
					if(map.containsKey(obj.id)){
						map.get(obj.id).num += obj.num;
					}else{
						map.put(obj.id, obj);
					}
				}
			}
		}
		return new ArrayList<>(map.values());
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
	 * 获取商品配置
	 * @param id
	 * @return
	 */
	public static PowerShopConfig getShopConfig(int id){
		return POWER_SHOP_CONFIG.get(id);
	}
	
	/**
	 * 获取本次商店刷新需要的点券
	 */
	public static int getShopFlushCostGoods(int flushNum){
		if(POWER_SHOP_FLUSH_CONFIG.containsKey(flushNum)){
			return POWER_SHOP_FLUSH_CONFIG.get(flushNum).getCostGoods(flushNum);
		}
		if(flushNum >= MAX_SHOP_FLUSH){
			return POWER_SHOP_FLUSH_CONFIG.get(-1).getCostGoods(flushNum);
		}
		return 0;
	}

	/**
	 * 初始化、重置商品列表
	 * @return
	 */
	public static Map<Integer,PowerShopVO> initShop() {
		Map<Integer,PowerShopVO> rewardMap = new ConcurrentHashMap<>();
		for(PowerShopConfig config : POWER_SHOP_CONFIG.values()){
			PowerShopVO loveShopVO = new PowerShopVO(config);
			rewardMap.put(loveShopVO.id,loveShopVO);
		}
		return rewardMap;
	}
	
	
	private static boolean isRank(int rank,String stringRank){
		List<Integer> list = StringUtils.delimiterString2IntList(stringRank, Splitable.ATTRIBUTE_SPLIT);
		if(list.get(0) <= rank && list.get(1) == -1){
			return true;
		}
		if(list.get(0) <= rank && rank <= list.get(1)){
			return true;
		}
		return false;
	}
	
	private String parserRank(int rankBegin,int rankEnd){
		return rankBegin + Splitable.ATTRIBUTE_SPLIT + rankEnd;
	}

	public static int getMaxRewardRank() {
		return MAX_REWARD_RANK;
	}
}
