package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.RechargeAppConfig;
import com.jtang.gameserver.dataconfig.model.RechargeConfig;

@Component
public class RechargeAppService extends ServiceAdapter {
	
	private static Map<Integer,RechargeAppConfig> RECHARGE_APP_CONFIG = new HashMap<>();
	
	private static Map<Integer,RechargeConfig> RECHARGE_MAP = new HashMap<>();

	@Override
	public void clear() {
		RECHARGE_APP_CONFIG.clear();
		RECHARGE_MAP.clear();
	}

	@Override
	public void initialize() {
		List<RechargeAppConfig> list = dataConfig.listAll(this,RechargeAppConfig.class);
		for(RechargeAppConfig config:list){
			RECHARGE_APP_CONFIG.put(config.rechargeId, config);
		}
		
		List<RechargeConfig> rechargeList = dataConfig.listAll(this, RechargeConfig.class);
		for(RechargeConfig config:rechargeList){
			RECHARGE_MAP.put(config.rechargeNum, config);
		}
	}
	
	public static RechargeAppConfig get(int recharegeId){
		return RECHARGE_APP_CONFIG.get(recharegeId);
	}
	
	public static RechargeConfig getRecharege(int recharegeNum){
		return RECHARGE_MAP.get(recharegeNum);
	}

}
