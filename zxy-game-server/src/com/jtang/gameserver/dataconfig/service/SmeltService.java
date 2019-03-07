package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.SmeltConfig;
import com.jtang.gameserver.dataconfig.model.SmeltExchangeConfig;
import com.jtang.gameserver.module.smelt.type.SmeltType;

@Component
public class SmeltService extends ServiceAdapter {
	
	private static Map<Integer,Map<Integer,SmeltConfig>> SMELT_MAP = new HashMap<>();
	
	private static Map<Integer,SmeltExchangeConfig> EXCHANGE_MAP = new HashMap<>();

	@Override
	public void clear() {
		SMELT_MAP.clear();
		EXCHANGE_MAP.clear();
	}

	@Override
	public void initialize() {
		List<SmeltConfig> smeltList = dataConfig.listAll(this, SmeltConfig.class);
		for(SmeltConfig config:smeltList){
			if(SMELT_MAP.containsKey(config.convertType)){
				SMELT_MAP.get(config.convertType).put(config.star, config);
			}else{
				Map<Integer,SmeltConfig> map = new HashMap<>();
				map.put(config.star, config);
				SMELT_MAP.put(config.convertType, map);
			}
		}
		
		List<SmeltExchangeConfig> exchangeList = dataConfig.listAll(this, SmeltExchangeConfig.class);
		for(SmeltExchangeConfig config:exchangeList){
			EXCHANGE_MAP.put(config.heroId, config);
		}
	}
	
	public static SmeltConfig getSmeltConfig(int star,boolean isHero){
		if(isHero){
			return SMELT_MAP.get(SmeltType.HERO.getType()).get(star);
		}else{
			return SMELT_MAP.get(SmeltType.SOUL.getType()).get(star);
		}
	}
	
	public static SmeltExchangeConfig getExchangeConfig(int heroId){
		return EXCHANGE_MAP.get(heroId);
	}

}
