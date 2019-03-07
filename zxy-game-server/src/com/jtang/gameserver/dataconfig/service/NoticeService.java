package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.NoticeConfig;
@Component
public class NoticeService  extends ServiceAdapter {
	private static Map<Integer, NoticeConfig> configMap = new HashMap<>();
	
	@Override
	public void clear() {
		configMap.clear();
	}
	
	@Override
	public void initialize() {
		List<NoticeConfig> noticeConfigList = dataConfig.listAll(this, NoticeConfig.class);
		for(NoticeConfig config : noticeConfigList){
			configMap.put(config.getType(), config);
		}
	}
	
	public static NoticeConfig get(int type){
		return configMap.get(type);
	}
	
}
