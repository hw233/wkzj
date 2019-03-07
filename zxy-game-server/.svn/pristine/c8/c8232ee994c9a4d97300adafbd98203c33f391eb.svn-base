package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.PraiseConfig;

/**
 * 吸灵室使用物品服务类
 * @author 0x737263
 *
 */
@Component
public class PraiseService extends ServiceAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(PraiseService.class);

	private static List<PraiseConfig> configs = new ArrayList<>();
	@Override
	public void clear() {
		configs.clear();
	}

	@Override
	public void initialize() {
		List<PraiseConfig> list = dataConfig.listAll(this, PraiseConfig.class);
		configs.addAll(list);
	}
	
	public static PraiseConfig get() {
		if (configs.size() > 0){
			return configs.get(0);
		}
		LOGGER.error("不存在配置");
		return null;
	}
	
}
