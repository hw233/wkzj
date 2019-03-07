package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.VipShopConfig;

@Component
public class VipShopService extends ServiceAdapter {
	
	private static Map<Integer,VipShopConfig> SHOP_MAP = new HashMap<>();

	@Override
	public void clear() {
		SHOP_MAP.clear();
	}

	@Override
	public void initialize() {
		List<VipShopConfig> list = dataConfig.listAll(this, VipShopConfig.class);
		for(VipShopConfig config:list){
			SHOP_MAP.put(config.id, config);
		}
	}
	
	public static VipShopConfig getConfig(int id){
		return SHOP_MAP.get(id);
	}

}
