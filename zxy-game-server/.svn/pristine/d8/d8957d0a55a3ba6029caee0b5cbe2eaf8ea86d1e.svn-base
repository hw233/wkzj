package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.RewardObject;
import com.jtang.gameserver.dataconfig.model.LoveConfig;
import com.jtang.gameserver.dataconfig.model.LoveFlushConfig;
import com.jtang.gameserver.dataconfig.model.LoveGlobalConfig;
import com.jtang.gameserver.dataconfig.model.LoveMonsterConfig;
import com.jtang.gameserver.dataconfig.model.LoveMonsterFlushConfig;
import com.jtang.gameserver.dataconfig.model.LoveMonsterGlobalConfig;
//import com.jtang.gameserver.dataconfig.model.LoveMonsterLeastConfig;
import com.jtang.gameserver.dataconfig.model.LoveMonsterRewardConfig;
import com.jtang.gameserver.dataconfig.model.LoveRankRewardConfig;
import com.jtang.gameserver.dataconfig.model.LoveShopConfig;
import com.jtang.gameserver.dataconfig.model.LoveShopGlobalConfig;
import com.jtang.gameserver.module.love.model.BossFightVO;
import com.jtang.gameserver.module.love.model.BossVO;
import com.jtang.gameserver.module.love.model.LoveShopVO;

/**
 * 结婚服务
 * @author ludd
 *
 */
@Component
public class LoveService extends ServiceAdapter {
	private static LoveConfig cfg;
	/** 排行榜配置 */
	private static LoveGlobalConfig GLOBAL_CONFIG;
	/** 排行榜奖励配置 */
	private static Map<String, LoveRankRewardConfig> LOVE_RANK_REWARD_CONFIG_MAP = new HashMap<>();
	/** 商店配置 */
	private static LoveShopGlobalConfig SHOP_CONFIG;
	/** 商店刷新消耗点券配置 */
	private static Map<Integer,LoveFlushConfig> FLUSH_CONFIG = new HashMap<>();
	/** 商店商品配置 */
	private static Map<Integer,LoveShopConfig> LOVE_SHOP_CONFIG = new HashMap<>();
	/** 仙侣合作怪物配置 */
	private static Map<Integer,LoveMonsterConfig> LOVE_MONSTER_CONFIG = new HashMap<>();
	/** 仙侣合作挑战次数刷新配置 */
	private static Map<Integer,LoveMonsterFlushConfig> LOVE_MONSTER_FLUSH_CONFIG = new HashMap<>();
	/** 仙侣合作全局配置 */
	private static Map<Integer,LoveMonsterGlobalConfig> LOVE_MONSTER_GLOBAL_CONFIG = new HashMap<>();
	/** 仙侣合作活动奖励配置 */
	private static Map<Integer,List<LoveMonsterRewardConfig>> LOVE_MONSTER_REWARD_CONFIG = new HashMap<>();
//	/** 仙侣合作额外奖励保底配置 */
//	private static Map<Integer,LoveMonsterLeastConfig> LOVE_MONSTER_LEAST_CONFIG = new HashMap<>();
	
	private static int SHOP_MAX_FLUSH_NUM = 0;
	
	private static int MONSTER_MAX_FLUSH_NUM = 0;
	
	@Override
	public void clear() {
		LOVE_RANK_REWARD_CONFIG_MAP.clear();
		FLUSH_CONFIG.clear();
		LOVE_SHOP_CONFIG.clear();
		LOVE_MONSTER_CONFIG.clear();
		LOVE_MONSTER_FLUSH_CONFIG.clear();
		LOVE_MONSTER_REWARD_CONFIG.clear();
//		LOVE_MONSTER_LEAST_CONFIG.clear();
	}
	
	@Override
	public void initialize() {
		List<LoveConfig> list = this.dataConfig.listAll(this, LoveConfig.class);
		for (LoveConfig loveConfig : list) {
			cfg = loveConfig;
			break;
		}
		
		List<LoveGlobalConfig> globalList = dataConfig.listAll(this, LoveGlobalConfig.class);
		for (LoveGlobalConfig globalConfig : globalList){
			GLOBAL_CONFIG = globalConfig;
		}
		
		List<LoveRankRewardConfig> rankList = dataConfig.listAll(this, LoveRankRewardConfig.class);
		for (LoveRankRewardConfig config : rankList) {
			LOVE_RANK_REWARD_CONFIG_MAP.put(config.rank, config);
		}
		
		List<LoveShopGlobalConfig> shopGlobalList = dataConfig.listAll(this, LoveShopGlobalConfig.class);
		for (LoveShopGlobalConfig config : shopGlobalList) {
			SHOP_CONFIG = config;
		}
		
		List<LoveFlushConfig> flushList = dataConfig.listAll(this, LoveFlushConfig.class);
		for (LoveFlushConfig config : flushList) {
			if(SHOP_MAX_FLUSH_NUM < config.flushNum){
				SHOP_MAX_FLUSH_NUM = config.flushNum;
			}
			FLUSH_CONFIG.put(config.flushNum, config);
		}
		
		List<LoveShopConfig> shopList = dataConfig.listAll(this, LoveShopConfig.class);
		for (LoveShopConfig config : shopList) {
			LOVE_SHOP_CONFIG.put(config.id, config);
		}
		
		List<LoveMonsterConfig> monsterConfig = dataConfig.listAll(this, LoveMonsterConfig.class);
		for(LoveMonsterConfig config : monsterConfig){
			LOVE_MONSTER_CONFIG.put(config.getId(), config);
		}
		
		List<LoveMonsterFlushConfig> monsterFlushConfig = dataConfig.listAll(this, LoveMonsterFlushConfig.class);
		for(LoveMonsterFlushConfig config : monsterFlushConfig){
			if(MONSTER_MAX_FLUSH_NUM < config.flushNum){
				MONSTER_MAX_FLUSH_NUM = config.flushNum;
			}
			LOVE_MONSTER_FLUSH_CONFIG.put(config.flushNum, config);
		}
		
		List<LoveMonsterGlobalConfig> monsterGlobalConfig = dataConfig.listAll(this, LoveMonsterGlobalConfig.class);
		for(LoveMonsterGlobalConfig config : monsterGlobalConfig){
			LOVE_MONSTER_GLOBAL_CONFIG.put(config.difficultId, config);
		}
		
		List<LoveMonsterRewardConfig> monsterRewardConfig = dataConfig.listAll(this, LoveMonsterRewardConfig.class);
		for(LoveMonsterRewardConfig config : monsterRewardConfig){
			if(LOVE_MONSTER_REWARD_CONFIG.containsKey(config.difficultId)){
				List<LoveMonsterRewardConfig> configList = LOVE_MONSTER_REWARD_CONFIG.get(config.difficultId);
				configList.add(config);
			}else{
				List<LoveMonsterRewardConfig> configList = new ArrayList<>();
				configList.add(config);
				LOVE_MONSTER_REWARD_CONFIG.put(config.difficultId, configList);
			}
		}
		
//		List<LoveMonsterLeastConfig> leastConfig = dataConfig.listAll(this, LoveMonsterLeastConfig.class);
//		for(LoveMonsterLeastConfig config : leastConfig){
//			LOVE_MONSTER_LEAST_CONFIG.put(config.id, config);
//		}
	}
	
	public static LoveConfig get() {
		return cfg;
	}
	
	/**
	 * 获取仙侣排行全局配置
	 * @return
	 */
	public static LoveGlobalConfig getGlobalConfig(){
		return GLOBAL_CONFIG;
	}
	
	
	/**
	 * 根据排名获得奖励配置
	 * @param rank
	 * @return
	 */
	public static LoveRankRewardConfig getRankReward(long rank){
		for(Entry<String,LoveRankRewardConfig> entry:LOVE_RANK_REWARD_CONFIG_MAP.entrySet()){
			LoveRankRewardConfig loveRewardConfig = entry.getValue();
			if(loveRewardConfig.startRank <= rank && rank <= loveRewardConfig.endRank){
				return loveRewardConfig;
			}
		}
		return null;
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
	
	private static List<Integer> getFlushTime(){
		List<Date> date = SHOP_CONFIG.getFlushTime();
		List<Integer> flushTime = new ArrayList<>();
		for(Date d : date){
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			Long time = c.getTimeInMillis() / 1000;
			flushTime.add(time.intValue());
		}
		return flushTime;
	}
	
	/**
	 * 获取本次商店刷新需要的点券
	 */
	public static int getFlushCostTicket(int flushNum){
		if(FLUSH_CONFIG.containsKey(flushNum)){
			return FLUSH_CONFIG.get(flushNum).getCostTicket(flushNum);
		}
		if(flushNum >= SHOP_MAX_FLUSH_NUM){
			return FLUSH_CONFIG.get(-1).getCostTicket(flushNum);
		}
		return 0;
	}
	
	/**
	 * 获取商品配置
	 * @param id
	 * @return
	 */
	public static LoveShopConfig getShopConfig(int id){
		return LOVE_SHOP_CONFIG.get(id);
	}

	/**
	 * 初始化、重置商品列表
	 * @return
	 */
	public static Map<Integer,LoveShopVO> initShop() {
		Map<Integer,LoveShopVO> rewardMap = new ConcurrentHashMap<>();
		for(LoveShopConfig config : LOVE_SHOP_CONFIG.values()){
			LoveShopVO loveShopVO = new LoveShopVO(config);
			rewardMap.put(loveShopVO.id,loveShopVO);
		}
		return rewardMap;
	}
	
	/**
	 * 获取仙侣商城排行配置
	 * @return
	 */
	public static LoveShopGlobalConfig getShopGlobalConfig(){
		return SHOP_CONFIG;
	}
	
	/**
	 * 获取仙侣合作全局配置
	 * @param id 难度id
	 * @return
	 */
	public static LoveMonsterConfig getLoveMonsterConfig(int id){
		return LOVE_MONSTER_CONFIG.get(id);
	}
	
	/**
	 * 获取仙侣合作刷新次数消耗的点券
	 * @param flushNum 点券刷新次数
	 * @return
	 */
	public static int getMonstFlushTicket(int flushNum){
		if(flushNum > MONSTER_MAX_FLUSH_NUM){
			return LOVE_MONSTER_FLUSH_CONFIG.get(-1).getCostTicket(flushNum);
		}
		if(LOVE_MONSTER_FLUSH_CONFIG.containsKey(flushNum)){
			return LOVE_MONSTER_FLUSH_CONFIG.get(flushNum).getCostTicket(flushNum);
		}
		return 0;
	}
	
	/**
	 * 获取仙侣合作难度全局配置
	 * @param id 难度id
	 * @return
	 */
	public static LoveMonsterGlobalConfig getLoveMonsterGlobalConfig(int id){
		return LOVE_MONSTER_GLOBAL_CONFIG.get(id);
	}
	
	/**
	 * 获取仙侣合作难度活动奖励
	 * @param id 难度id
	 * @return
	 */
	public static List<RewardObject> getLoveMonsterReward(int id){
		List<LoveMonsterRewardConfig> configList = LOVE_MONSTER_REWARD_CONFIG.get(id);
		List<RewardObject> list = new ArrayList<>();
		for (LoveMonsterRewardConfig config : configList) {
			if(config.isReward()){
				list.addAll(config.getReward());
			}
		}
		return list;
	}
	
	public static LoveMonsterRewardConfig getLoveMonsterRewardConfig(int id) {
		List<LoveMonsterRewardConfig> configList = LOVE_MONSTER_REWARD_CONFIG.get(id);
		for (LoveMonsterRewardConfig config : configList) {
			if(config.isReward()){
				return config;
			}
		}
		return null;
	}

	/**
	 * 初始化怪物数据
	 * @param level 玩家等级和
	 * @return
	 */
	public static Map<Integer, BossVO> initMonster(int level) {
		Map<Integer,BossVO> map = new HashMap<>();
		for(LoveMonsterConfig config : LOVE_MONSTER_CONFIG.values()){
			BossVO bossVO = new BossVO();
			bossVO.id = config.getId();
			Map<Integer,Integer> hpMap = new ConcurrentHashMap<>();
			int maxHp = 0;
			for(Integer monsterId : config.getMonsterList().values()){
				hpMap.put(monsterId, config.getMonsterHp(level, monsterId));
				maxHp += hpMap.get(monsterId);
			}
			bossVO.maxHp = maxHp;
			bossVO.monsterLastHpMax = new ConcurrentHashMap<>();
			bossVO.monsterLastHpMax.putAll(hpMap);
			bossVO.monsterHPMap = hpMap;
			map.put(config.getId(), bossVO);
		}
		return map;
	}

	/**
	 * 初始化玩家攻击怪物数据
	 * @return
	 */
	public static Map<Integer, BossFightVO> initFight() {
		Map<Integer,BossFightVO> map = new HashMap<>();
		for(LoveMonsterConfig config : LOVE_MONSTER_CONFIG.values()){
			BossFightVO bossVO = new BossFightVO();
			bossVO.id = config.getId();
			bossVO.state = 0;
			map.put(config.getId(), bossVO);
		}
		return map;
	}
	
	/**
	 * 获取所有难度id
	 */
	public static Collection<Integer> getAllId(){
		return LOVE_MONSTER_GLOBAL_CONFIG.keySet();
	}
	
//	/**
//	 * 获取保底配置
//	 */
//	public static LoveMonsterLeastConfig getLeastConfig(int id){
//		return LOVE_MONSTER_LEAST_CONFIG.get(id);
//	}
}
