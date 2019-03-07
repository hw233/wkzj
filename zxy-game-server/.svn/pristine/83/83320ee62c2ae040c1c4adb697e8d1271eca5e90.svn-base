package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.TaskConfig;

@Component
public class TaskService extends ServiceAdapter{
	
	private static int firstTaskId = 0;
	
	private static Map<Integer, TaskConfig> TASK_CONFIG_MAP = new HashMap<Integer, TaskConfig>();
	
	private static Map<Integer, List<Integer>> EVENKEY_TASKID_MAP = new HashMap<Integer, List<Integer>>();
	
	@Override
	public void clear() {
		TASK_CONFIG_MAP.clear();
		EVENKEY_TASKID_MAP.clear();
	}
	
	@Override
	public void initialize() {
		firstTaskId = 0;
		List<TaskConfig> configList = this.dataConfig.listAll(this, TaskConfig.class);
		for (TaskConfig config : configList) {
			if(firstTaskId == 0){
				firstTaskId = config.getTaskId();
			}
			if(firstTaskId > config.getTaskId()){
				firstTaskId = config.getTaskId();
			}
			TASK_CONFIG_MAP.put(config.getTaskId(), config);
			if(EVENKEY_TASKID_MAP.containsKey(config.getTaskType())){
				EVENKEY_TASKID_MAP.get(config.getTaskType()).add(config.getTaskId());
			}
			else{
				List<Integer> taskIds = new LinkedList<>();
				taskIds.add(config.getTaskId());
				EVENKEY_TASKID_MAP.put(config.getTaskType(), taskIds);
			}
		}
	}

	public static TaskConfig get(int taskId){
		return TASK_CONFIG_MAP.get(taskId);
	}

	public static List<Integer> getTaskIds(int taskType){
		return EVENKEY_TASKID_MAP.get(taskType);
	}
	public static TaskConfig getFirstTask(){
		return TASK_CONFIG_MAP.get(firstTaskId);
	}
}
