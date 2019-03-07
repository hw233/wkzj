package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.MonthCardConfig;

@Component
public class MonthCardService extends ServiceAdapter {

	private static Map<Integer,MonthCardConfig> MONTH_CARD = new HashMap<Integer,MonthCardConfig>();
	
	@Override
	public void clear() {
		MONTH_CARD.clear();
	}

	@Override
	public void initialize() {
		List<MonthCardConfig> list = dataConfig.listAll(this, MonthCardConfig.class);
		for (MonthCardConfig config : list) {
			MONTH_CARD.put(config.rechargeType, config);
		}
	}
	
	public static MonthCardConfig getConfig(int rechargeType){
		return MONTH_CARD.get(rechargeType);
	}

}
