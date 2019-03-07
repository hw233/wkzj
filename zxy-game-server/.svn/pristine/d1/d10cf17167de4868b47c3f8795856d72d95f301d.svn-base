package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.DelveConfig;

/**
 * 潜修房配置服务类
 * @author ludd
 *
 */
@Component
public class DelveService extends ServiceAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelveService.class);
	
	private static Map<Integer, DelveConfig> DELVE_CFG_LIST = new HashMap<>(); 
	
	private static List<DelveConfig> allList = new ArrayList<>();
//	
//	/**
//	 * 潜修室最大等级
//	 */
//    private static int DELVE_MAX_LEVEL = 0;
    
	@Override
	public void clear() {
		DELVE_CFG_LIST.clear();
		allList.clear();
	}
    
	@Override
	public void initialize() {
		allList.addAll(dataConfig.listAll(this, DelveConfig.class));
		for (DelveConfig delveConfig : allList) {
			DELVE_CFG_LIST.put(delveConfig.type, delveConfig);
		}
//		DELVE_MAX_LEVEL = DELVE_CFG_LIST.size();
	}
	/**
	 * 获取修炼室配置
	 * @param level 等级
	 * @return 如果配置中不存在该等级的配置，返回null
	 */
	public static DelveConfig get(int level){
		if (DELVE_CFG_LIST.containsKey(level)){
			return DELVE_CFG_LIST.get(level);
		}
		
		LOGGER.error(String.format("DelveConfig缺少配置，level: [%s]", level));
		return null;
	}
	
	/**
	 * 获取所有配置
	 * @return
	 */
	public static List<DelveConfig> getList(){
		return allList;
	}
	
//	/**
//	 * 潜修室最大等级
//	 * @return
//	 */
//	public static int delveMaxLevel(){
//		return DELVE_MAX_LEVEL;
//	}
	
}
