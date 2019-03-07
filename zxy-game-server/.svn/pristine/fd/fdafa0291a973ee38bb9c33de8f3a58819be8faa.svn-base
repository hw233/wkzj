package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.GiftConfig;

/**
 * 礼物配置服务类
 * @author vinceruan
 *
 */
@Component
public class GiftService extends ServiceAdapter {

	private static final Map<Integer, GiftConfig> GIFT_CONFIG_MAP = new HashMap<Integer, GiftConfig>();
	
	@Override
	public void clear() {
		GIFT_CONFIG_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<GiftConfig> list = this.dataConfig.listAll(this, GiftConfig.class);
		for (GiftConfig conf : list) {
			GIFT_CONFIG_MAP.put(conf.getActorLevel(), conf);
		}
	}
	
	public static GiftConfig get(int level) {
		return GIFT_CONFIG_MAP.get(level);
	}
}
