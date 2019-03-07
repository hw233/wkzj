package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.VampiirConfig;
import com.jtang.gameserver.dataconfig.model.VampiirExchangeConfig;
import com.jtang.gameserver.dataconfig.model.VampiirStarConfig;
/**
 * 吸灵室升级配置的初始化...并提供相关数据访问接口
 * @author pengzy
 *
 */
@Component
public class VampiirService extends ServiceAdapter{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VampiirService.class);
	
	private static Map<Integer, VampiirConfig> VAMPIIR_UP_LIST = new HashMap<>();
	
	private static Map<Integer, VampiirExchangeConfig> VAMPIIR_EXCHANGE_LIST = new HashMap<>(); 

	@Override
	public void clear() {
		VAMPIIR_UP_LIST.clear();
		VAMPIIR_EXCHANGE_LIST.clear();
	}
	
	@Override
	public void initialize() {
		List<VampiirExchangeConfig> exchangeConfigs = dataConfig.listAll(this, VampiirExchangeConfig.class);
		for(VampiirExchangeConfig config : exchangeConfigs){
			VAMPIIR_EXCHANGE_LIST.put(config.star, config);
		}
		
		List<VampiirConfig> configs = dataConfig.listAll(this, VampiirConfig.class);
		for(VampiirConfig vampiirConfig : configs){
			VAMPIIR_UP_LIST.put(vampiirConfig.getLevel(), vampiirConfig);
		}
	}
	
	/**
	 * 根据星级获得兑换配置
	 */
	public static VampiirExchangeConfig getExchangeConfig(int star){
		return VAMPIIR_EXCHANGE_LIST.get(star);
	}
	
	/**
	 * 按吸灵室等级获取相对应的配置
	 * @param level
	 * @return
	 */
	public static VampiirConfig get(int level){
		if (VAMPIIR_UP_LIST.containsKey(level)){
			return VAMPIIR_UP_LIST.get(level);
		}
		LOGGER.error(String.format("VampiirConfig缺少配置，level: [%s]", level));
		return null;
	}
//	
	/**
	 * 吸灵室增加的百分比
	 * @param level		吸灵室等级
	 * @return
	 */
	public static float getIncreaseExp(int level){
		if(VAMPIIR_UP_LIST.containsKey(level)){
			return VAMPIIR_UP_LIST.get(level).getIncreaseExp() / 100.0f;
		}
		LOGGER.error(String.format("VampiirConfig缺少配置，level: [%s]", level));
		return 1;
	}
	
	/**
	 * 返回吸灵堂可升级到的最高等级
	 * @return
	 */
	public static int getMaxVampiirLevel(){
		return VAMPIIR_UP_LIST.size();
	}
	
	/**
	 * 被吸仙人经验计算
	 * @param heroStar			仙人星级
	 * @param totalExp			当前仙人总经验
	 * @param vampiirLevelRate	吸灵室附加百分比
	 * @return
	 */
	public static int vampiirHeroExp(int heroStar, int totalExp, float vampiirLevelRate) {
		VampiirStarConfig starConfig = VampiirStarService.get(heroStar);

		// 仙人经验计算 = 不同品质仙人基础经验 *（1+吸灵室经验百分比）+总经验*（基础返还百分比+吸灵室经验百分比）
		return (int) (starConfig.getHeroExp() * (1 + vampiirLevelRate) + totalExp * (starConfig.getProportion() + vampiirLevelRate));
	}
	
	/**
	 * 被吸魂魄经验计算
	 * @param heroStar			魂魄星级
	 * @param soulNum			魂魄数量
	 * @param vampiirLevelRate	吸灵室附加百分比
	 * @return
	 */
	public static int vampiirSoulExp(int heroStar, int soulNum, float vampiirLevelRate) {
		VampiirStarConfig starConfig = VampiirStarService.get(heroStar);

		// 魂魄经验计算 = 不同品质仙人基础经验 * 魂魄数 *（1+吸灵室经验加成百分比）
		return (int) (starConfig.getHeroSoulExp() * soulNum * (1 + vampiirLevelRate));
	}
	
}
