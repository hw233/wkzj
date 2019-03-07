package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.DailyTaskRewardConfig;
import com.jtang.gameserver.dataconfig.model.DailyTasksConfig;

@Component
public class DailyTaskService extends ServiceAdapter{
	
	
	private static Map<Integer, DailyTasksConfig> TASK_CONFIG_MAP = new HashMap<Integer, DailyTasksConfig>();
	
	private static Map<Integer, List<DailyTasksConfig>> TASK_TYPE_MAP = new HashMap<>();
	
	private static Map<Integer, DailyTaskRewardConfig> REWARD_MAP = new HashMap<>();
	@Override
	public void clear() {
		TASK_CONFIG_MAP.clear();
		TASK_TYPE_MAP.clear();
		REWARD_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<DailyTasksConfig> configList = this.dataConfig.listAll(this, DailyTasksConfig.class);
		for (DailyTasksConfig config : configList) {
			TASK_CONFIG_MAP.put(config.getTaskId(), config);
			List<DailyTasksConfig> list = null;
			if (TASK_TYPE_MAP.containsKey(config.getTaskParser())) {
				list = TASK_TYPE_MAP.get(config.getTaskParser());
			} else {
				list = new ArrayList<>();
				TASK_TYPE_MAP.put(config.getTaskParser(), list);
			}
			list.add(config);
			
 		}
		
		List<DailyTaskRewardConfig> list = this.dataConfig.listAll(this, DailyTaskRewardConfig.class);
		for (DailyTaskRewardConfig dailyTaskRewardConfig : list) {
			REWARD_MAP.put(dailyTaskRewardConfig.getId(), dailyTaskRewardConfig);
		}
	}

	public static DailyTasksConfig get(int taskId){
		return TASK_CONFIG_MAP.get(taskId);
	}
	
	public static List<DailyTasksConfig> getDailyTasksConfigByType(int type) {
		return TASK_TYPE_MAP.get(type);
	}
	
	public static List<Integer> getTaskIds() {
		List<Integer> list = new ArrayList<>();
		for (DailyTasksConfig cfg : TASK_CONFIG_MAP.values()) {
			list.add(cfg.getTaskId());
		}
		return list;
	}
	
	public static DailyTaskRewardConfig getRewardConfig(int id) {
		return REWARD_MAP.get(id);
	}

}
