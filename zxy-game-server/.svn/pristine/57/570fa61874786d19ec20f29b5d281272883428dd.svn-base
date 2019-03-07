package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.GlobalConfig;

/**
 * 全局配置服务类
 * @author 0x737263
 *
 */
@Component
public class GlobalService extends ServiceAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalService.class);
	
	private static Map<String, String> GLOBAL_CONFIG_MAPS = new HashMap<>();

	@Override
	public void clear() {
		GLOBAL_CONFIG_MAPS.clear();
	}
	
	@Override
	public void initialize() {
		List<GlobalConfig> list = dataConfig.listAll(this, GlobalConfig.class);
		for (GlobalConfig config : list) {
			GLOBAL_CONFIG_MAPS.put(config.getName(),config.getValue());
		}
	}
	
	/**
	 * 获取全局值
	 * @param name
	 * @return
	 */
	public static String get(String name) {
		if (GLOBAL_CONFIG_MAPS.containsKey(name)) {
			return GLOBAL_CONFIG_MAPS.get(name);
		}
		LOGGER.warn(String.format("global config name:[%s] not found.", name));
		return "";
	}
	
	public static int getInt(String name) {
		return Integer.valueOf(get(name));
	}
	
	public static byte getByte(String name) {
		return Byte.valueOf(get(name));
	}
}
