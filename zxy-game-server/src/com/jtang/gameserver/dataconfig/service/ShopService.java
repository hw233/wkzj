package com.jtang.gameserver.dataconfig.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.ShopConfig;

@Component
public class ShopService extends ServiceAdapter {

	private static Map<Integer,ShopConfig> SHOP_MAPS = new HashMap<Integer,ShopConfig>();
	
	@Override
	public void clear() {
		SHOP_MAPS.clear();
	}
	
	@Override
	public void initialize() {
		List<ShopConfig> list=this.dataConfig.listAll(this, ShopConfig.class);
		for(ShopConfig shopConfig:list){
			SHOP_MAPS.put(shopConfig.getShopId(), shopConfig);
		}
	}
	
	public static ShopConfig getShop(int shopId){
		return SHOP_MAPS.get(shopId);
	}
	
	public static Collection<ShopConfig> getAllConfig(){
		return SHOP_MAPS.values();
	}

}
