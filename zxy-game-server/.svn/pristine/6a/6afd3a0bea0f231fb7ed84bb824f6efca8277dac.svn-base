package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.EnhancedConfig;
@Component
public class EnhancedService extends ServiceAdapter {

	private static List<EnhancedConfig> ENHANCED_CONFIG = new ArrayList<EnhancedConfig>();

	@Override
	public void clear() {
		ENHANCED_CONFIG.clear();
	}
	
	@Override
	public void initialize() {
		ENHANCED_CONFIG.addAll(dataConfig.listAll(this, EnhancedConfig.class));
	}

	/**
	 * 获取强化室升级配置
	 * 
	 * @param level
	 * @return
	 */
	public static EnhancedConfig get() {
		return ENHANCED_CONFIG.get(0);
	}
	
	
	public static int getMaxLevel(){
		return ENHANCED_CONFIG.size();
	}
}
