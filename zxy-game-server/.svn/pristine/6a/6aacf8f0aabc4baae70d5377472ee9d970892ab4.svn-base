package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.SnatchRobotConfig;

/**
 * 抢夺机器人配置
 * @author liujian
 *
 */
@Component
public class SnatchRobotService extends ServiceAdapter {
	/**
	 *  抢夺配置,格式是Map<actorLevel, List<SnatchRobotConfig>>
	 */
	private static Map<Integer, List<SnatchRobotConfig>> SNATCH_ROBOT_CFG_LIST = new HashMap<>();
	
	/**
	 * 每个等级的气势值  
	 */
	private static Map<Integer,Integer> SNATCH_ROBOT_MIN_MORALE_MAP = new HashMap<>();
	
	/**
	 * 每个等级的气势值  
	 */
	private static Map<Integer,Integer> SNATCH_ROBOT_MAX_MORALE_MAP = new HashMap<>();
	
	private static int robotMaxLevel = 0;
	
	@Override
	public void clear() {
		SNATCH_ROBOT_CFG_LIST.clear();
		SNATCH_ROBOT_MIN_MORALE_MAP.clear();
		SNATCH_ROBOT_MAX_MORALE_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<SnatchRobotConfig> list = dataConfig.listAll(this, SnatchRobotConfig.class);		
		for (SnatchRobotConfig snatch : list) {
			List<SnatchRobotConfig> valueList = SNATCH_ROBOT_CFG_LIST.get(snatch.actorLevel);
			if(valueList == null) {
				valueList = new ArrayList<>();
			}
			valueList.add(snatch);
			SNATCH_ROBOT_CFG_LIST.put(snatch.actorLevel, valueList);
			if(snatch.actorLevel > robotMaxLevel){
				robotMaxLevel = snatch.actorLevel;
			}
			
			//TODO 先暂时这样用着以后要把robot的几个对象做一次重整的 
			SNATCH_ROBOT_MIN_MORALE_MAP.put(snatch.actorLevel, snatch.robotMaxMorale);
			SNATCH_ROBOT_MAX_MORALE_MAP.put(snatch.actorLevel, snatch.robotMaxMorale);
		}
	}
	
	public static int getRobotMaxLevel() {
		return robotMaxLevel;
	}
	
	/**
	 * 获取某等级的机器人列表
	 * @param actorLevel
	 * @return
	 */
	public static List<SnatchRobotConfig> getList(int actorLevel) {
		return SNATCH_ROBOT_CFG_LIST.get(actorLevel);
	}
	
	/**
	 * 根据等及获取随机气势值
	 */
	public static int randomMorale(int actorLevel) {
		return RandomUtils.nextInt(SNATCH_ROBOT_MIN_MORALE_MAP.get(actorLevel), SNATCH_ROBOT_MAX_MORALE_MAP.get(actorLevel));
	}

}
