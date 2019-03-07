package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.RefineConfig;
import com.jtang.gameserver.module.refine.type.RefineType;

/**
 * 精炼室配置服务类
 * @author 0x737263
 *
 */
@Component
public class RefineService extends ServiceAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(RefineService.class);
	
	private static Map<Integer, RefineConfig> REFINEROOM_CFG_LIST = new HashMap<>(); 
	
//	/**
//	 * 精炼室等级上限
//	 */
//	private static int maxLevel = 1;
	
	@Override
	public void clear() {
		REFINEROOM_CFG_LIST.clear();
	}
	
	@Override
	public void initialize() {
		List<RefineConfig> list = dataConfig.listAll(this, RefineConfig.class);
		for (RefineConfig cfg : list) {
			REFINEROOM_CFG_LIST.put(cfg.getType(), cfg);
//			if(cfg.getLevel() > maxLevel) {
//				maxLevel = cfg.getLevel();
//			}
		}
	}

	/**
	 * 获取精炼室配置
	 * @param roomLevel
	 * @return
	 */
	public static RefineConfig get(RefineType type) {
		if (REFINEROOM_CFG_LIST.containsKey(type.getCode())) {
			return REFINEROOM_CFG_LIST.get(type.getCode());
		}
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn(String.format("RefineConfig缺少配置，type: [%s]", type.getCode()));
		}
		return null;
	}
	
//	/**
//	 * 获取最大等级上限
//	 */
//	public static int maxLevel() {
//		return maxLevel;
//	}
	 
}
