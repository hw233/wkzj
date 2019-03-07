package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.ModuleControlConfig;

@Component
public class ModuleControlService extends ServiceAdapter{
	
	
	private static Map<Byte, ModuleControlConfig> CONFIG_MAP = new HashMap<>();
	
	@Override
	public void clear() {
		CONFIG_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<ModuleControlConfig> configList = this.dataConfig.listAll(this, ModuleControlConfig.class);
		for (ModuleControlConfig config : configList) {
			CONFIG_MAP.put((byte)config.getModuleId(), config);
 		}
		
	}
	public static boolean isOpen(byte moduleId) {
		if (CONFIG_MAP.containsKey(moduleId)) {
			ModuleControlConfig cfg = CONFIG_MAP.get(moduleId);
			return cfg.isOpen();
		}
		return false;
	}
}
