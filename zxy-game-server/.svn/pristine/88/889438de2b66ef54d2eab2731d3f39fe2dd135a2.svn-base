package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.RefineEquipConfig;

/**
 * 装备配置服务类
 * @author 0x737263
 *
 */
@Component
public class RefineEquipService extends ServiceAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(RefineEquipService.class);
	
	private static Map<String, RefineEquipConfig> REFINE_CFG_LIST = new HashMap<>(); 
	
	@Override
	public void clear() {
		REFINE_CFG_LIST.clear();
	}
	
	@Override
	public void initialize() {
		List<RefineEquipConfig> list = dataConfig.listAll(this, RefineEquipConfig.class);
		for (RefineEquipConfig refine : list) {
			REFINE_CFG_LIST.put(getKey(refine.getEquipStar(), refine.getEquipType()), refine);
		}
	}
	
	public static String getKey(int equipStar, int equipType){
		return equipStar + "_" + equipType;
	}
	
	/**
	 * 获取装备配置
	 * @param equipId
	 * @return
	 */
	public static RefineEquipConfig get(int equipStar, int equipType) {
		String key = getKey(equipStar, equipType);
		if (REFINE_CFG_LIST.containsKey(key)) {
			return REFINE_CFG_LIST.get(key);
		}
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn(String.format("RefineEquipConfig缺少配置，equipId: [%s]", key));
		}
		return null;
	}
	
}
