package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.RechargeRateConfig;
@Component
public class RechargeRateService extends ServiceAdapter {

	private static List<RechargeRateConfig> list = new ArrayList<>();
	
	@Override
	public void clear() {
		list.clear();
	}
	
	@Override
	public void initialize() {
		List<RechargeRateConfig> dataList = dataConfig.listAll(this, RechargeRateConfig.class);
		RechargeRateConfig[] dataArr = new RechargeRateConfig[dataList.size()];
		dataList.toArray(dataArr);
		Arrays.sort(dataArr);
		for (RechargeRateConfig rechargeRateConfig : dataArr) {
			list.add(rechargeRateConfig);
		}
		
	}
	
	public static int getGiveTicket(int rechargeId){
		for (int i = list.size() - 1; i >= 0; i--) {
			RechargeRateConfig rechargeRateConfig = list.get(i);
			if (rechargeId == rechargeRateConfig.rechargeId){
				return rechargeRateConfig.give;
			}
		}
		return 0;
	}

	public static RechargeRateConfig getConfig(int goodsId) {
		for(RechargeRateConfig config:list){
			if(config.rechargeId == goodsId){
				return config;
			}
		}
		return null;
	}

}
