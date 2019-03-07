package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.RewardObject;
import com.jtang.gameserver.dataconfig.model.ActorBuyConfig;
import com.jtang.gameserver.dataconfig.model.ActorEnergyConfig;
import com.jtang.gameserver.dataconfig.model.ActorUpgradeConfig;
import com.jtang.gameserver.dataconfig.model.ActorVitConfig;
import com.jtang.gameserver.module.user.type.EnergyAddType;
import com.jtang.gameserver.module.user.type.VITAddType;

/**
 * 角色服务类
 * @author 0x737263
 *
 */
@Component
public class ActorService extends ServiceAdapter {
	
	/**
	 * 角色升级配置集合 key:等级  value:升级配置
	 */
	private static Map<Integer,ActorUpgradeConfig> ACTOR_UPGRADE_MAPS = new HashMap<>();
	
	/**
	 * 角色增加精力忽略上限类型
	 */
	private static List<ActorEnergyConfig> ENERGY_LIST = new ArrayList<>();
	
	/**
	 * 角色增加活力上限类型
	 */
	private static List<ActorVitConfig> VIT_LIST = new ArrayList<>();
	
	/**
	 * 主页购买精、活、金币配置
	 * <类型:<次数:配置>>
	 */
	private static Map<Integer,Map<Integer,ActorBuyConfig>> ACTOR_BUY_MAP = new HashMap<>();

	/**
	 * 角色等级上限
	 */
	private static int maxLevel = 1;
	
	@Override
	public void clear() {
		ACTOR_UPGRADE_MAPS.clear();
		ENERGY_LIST.clear();
		VIT_LIST.clear();
	}
	
	@Override
	public void initialize() {		
		List<ActorUpgradeConfig> list = dataConfig.listAll(this, ActorUpgradeConfig.class);
		for (ActorUpgradeConfig config : list) {
			ACTOR_UPGRADE_MAPS.put(config.getLevel(), config);
			
			if(config.getLevel() > maxLevel) {
				maxLevel = config.getLevel();	
			}
		}
		
		ENERGY_LIST.addAll(dataConfig.listAll(this, ActorEnergyConfig.class));
		
		VIT_LIST.addAll(dataConfig.listAll(this, ActorVitConfig.class));
		
		List<ActorBuyConfig> buyList = dataConfig.listAll(this, ActorBuyConfig.class);
		for(ActorBuyConfig config:buyList){
			if(ACTOR_BUY_MAP.containsKey(config.type)){
				ACTOR_BUY_MAP.get(config.type).put(config.count, config);
			}else{
				Map<Integer,ActorBuyConfig> configMap = new HashMap<>();
				configMap.put(config.count, config);
				ACTOR_BUY_MAP.put(config.type, configMap);
			}
		}
	}
	
	/**
	 * 获取角色升级配置 
	 * @param actorLevel	当前角色等级
	 * @return
	 */
	public static ActorUpgradeConfig getUpgradeConfig(int actorLevel) {
		return ACTOR_UPGRADE_MAPS.get(actorLevel);
	}
	
	/**
	 * 获取最大等级上限
	 */
	public static int maxLevel() {
		return maxLevel;
	}
	
	/**
	 * 获取仙人升级的上限
	 * @param actorLevel	 当前角色等级
	 * @return
	 */
	public static int getHeroLevelLimit(int actorLevel) {
		ActorUpgradeConfig config = getUpgradeConfig(actorLevel);
		if (config == null) {
			return 0;
		}
		return config.getHeroLevelLimit();
	}
		
	/**
	 * 是否超出英雄等级上限
	 * @param actorLevel	当前角色等级
	 * @param heroLevel		当前仙人等级
	 * @return
	 */
	public static boolean isHeroLevelLimit(int actorLevel, int heroLevel) {
		ActorUpgradeConfig config = getUpgradeConfig(actorLevel);
		if (config == null) {
			return true;
		}
		return heroLevel > config.getHeroLevelLimit() ? true : false;
	}
	
	/**
	 * 获取角色可升级的等级数
	 * @param actorLevel			当前角色等级
	 * @param totalTeputation		当前累计声望值(当前声望+新增的声望)
	 * @return
	 */
	public static int getAbleUpgrades(int actorLevel, long totalTeputation) {
		int ableUpgrades = 0;
		
		long needReputation = 0;
		while (actorLevel + ableUpgrades < maxLevel) {
			ActorUpgradeConfig config = getUpgradeConfig(actorLevel + ableUpgrades);
			needReputation = config.getNeedReputation();
			if(totalTeputation < needReputation) {
				break;
			}
			totalTeputation -= needReputation;
			ableUpgrades++;
		}
		return ableUpgrades;
	}
	
	/**
	 * 升级后可奖励的物品
	 * @param actorLevel
	 * @return
	 */
	public static List<RewardObject> getRewardList(int actorLevel) {
		ActorUpgradeConfig config = getUpgradeConfig(actorLevel);
		if (config == null) {
			return new ArrayList<>();
		}
		return config.getRewardList();
	}
	
	/**
	 * 获取抢夺兑换券上限
	 * @param actorLevel	角色等级
	 * @param voucher		角色当前兑换券数
	 * @return
	 */
	public static int getSnatchVoucherLimit(int actorLevel, int voucher) {
		ActorUpgradeConfig upConf = ActorService.getUpgradeConfig(actorLevel);
		return Math.min(voucher, upConf.getSnatchVoucherLimit());
	}
	
	/**
	 * 是否可以超过精力上限
	 */
	public static boolean isEnergy(EnergyAddType type){
		for(ActorEnergyConfig config:ENERGY_LIST){
			if(type.getId() == config.id){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否可以超过活力上限
	 */
	public static boolean isVit(VITAddType type){
		for(ActorVitConfig config:VIT_LIST){
			if(type.getId() == config.id){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取本次消耗点券
	 * @param type 类型
	 * @param count 已用次数
	 * @param configNum 配置次数 
	 */
	public static int getTicket(int type,int count, int configNum){
		if(configNum == -1){//金币V13+特殊处理，无限购买
			int buyCount = 0;
			Map<Integer,ActorBuyConfig> map = ACTOR_BUY_MAP.get(type);
			for(Integer key:map.keySet()){
				if(key > buyCount){
					buyCount = key;
				}
			}
			if(count >= buyCount){
				return ACTOR_BUY_MAP.get(type).get(buyCount).ticket;
			}
		}
		return ACTOR_BUY_MAP.get(type).get(count + 1).ticket;
	}
	
}
