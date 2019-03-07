package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.DemonExchangeConfig;

/**
 * 集众降魔积分服务
 * @author ludd
 *
 */
@Component
public class DemonExchangeService extends ServiceAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(DemonExchangeService.class);
	
	/**
	 * key:配置id
	 */
	private static Map<Integer, DemonExchangeConfig> MAP = new HashMap<>(); 
	
	@Override
	public void clear() {
		MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<DemonExchangeConfig> list = dataConfig.listAll(this, DemonExchangeConfig.class);
		for (DemonExchangeConfig demonExchangeConfig : list) {
			MAP.put(demonExchangeConfig.getId(), demonExchangeConfig);
		}
	}
	
	public static DemonExchangeConfig get(int id) {
		if (MAP.containsKey(id) == false) {
			LOGGER.error(String.format("不存在配置id:[%s]", id));
			return null;
		}
		
		return MAP.get(id);
	}
	
	


}
