package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.SysmailConfig;

@Component
public class SysmailService extends ServiceAdapter {

	static Map<Integer, SysmailConfig> SYSMAIL_CONFIG_MAP = new HashMap<Integer, SysmailConfig>();

	@Override
	public void clear() {
		SYSMAIL_CONFIG_MAP.clear();
	}

	@Override
	public void initialize() {
		List<SysmailConfig> list = dataConfig.listAll(this, SysmailConfig.class);
		for (SysmailConfig config : list) {
			SYSMAIL_CONFIG_MAP.put(config.id, config);
		}
	}

	public static SysmailConfig get(int id) {
		return SYSMAIL_CONFIG_MAP.get(id);
	}

}
