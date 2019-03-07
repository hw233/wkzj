package com.jtang.worldserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.worldserver.dataconfig.model.CrossBattleDayConfig;

@Component
public class CrossBattleDayService extends ServiceAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CrossBattleDayService.class);
	/**
	 * key：第几天
	 */
	private static Map<Integer, List<CrossBattleDayConfig>> map = new HashMap<Integer, List<CrossBattleDayConfig>>();
	
	private static int totalDay;

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public void initialize() {
		List<CrossBattleDayConfig> list = dataConfig.listAll(this, CrossBattleDayConfig.class);
		
		for (CrossBattleDayConfig crossBattleDayConfig : list) {
			List<CrossBattleDayConfig> cfgList = null;
			if (map.containsKey(crossBattleDayConfig.getDayNum())) {
				cfgList = map.get(crossBattleDayConfig.getDayNum());
			} else {
				cfgList = new ArrayList<>();
				map.put(crossBattleDayConfig.getDayNum(), cfgList);
			}
			cfgList.add(crossBattleDayConfig);
			totalDay = crossBattleDayConfig.getDayNum() > totalDay ? crossBattleDayConfig.getDayNum() : totalDay;
		}
	}
	
	public static CrossBattleDayConfig get(int dayNum, int serverId) {
		List<CrossBattleDayConfig> cfgList = map.get(dayNum);
		if (cfgList != null) {
			for (CrossBattleDayConfig crossBattleDayConfig : cfgList) {
				if (crossBattleDayConfig.getHomeServerId() == serverId || crossBattleDayConfig.getAwayServerId() == serverId) {
					return crossBattleDayConfig;
				}
			}
		}
		LOGGER.error(String.format("配置不存在, dayNum:[%s], serverId:[%s]", dayNum, serverId));
		return null;
	}
	
	public static Set<Integer> getDayServerId(int dayNum) {
		List<CrossBattleDayConfig> list = map.get(dayNum);
		Set<Integer> result = new HashSet<>();
		for (CrossBattleDayConfig crossBattleDayConfig : list) {
			result.add(crossBattleDayConfig.getHomeServerId());
		}
		return result;
	}
	
	public static int getTotalDay() {
		return totalDay;
	}
	
	public static int getOtherServerId(int dayNum, int serverId) {
		int result = 0;
		List<CrossBattleDayConfig> cfgList = map.get(dayNum);
		if (cfgList == null) {
			LOGGER.error(String.format("不存在配置,dayNum:[%s], serverId:[%s]", dayNum, serverId));
			return 0;
		}
		for (CrossBattleDayConfig crossBattleDayConfig : cfgList) {
			if (crossBattleDayConfig.getHomeServerId() == serverId) {
				result = crossBattleDayConfig.getAwayServerId();
			} else if(crossBattleDayConfig.getAwayServerId() == serverId) {
				result = crossBattleDayConfig.getHomeServerId();
			}
		}
		return result;
	}
	
	/**
	 * 获取今日对阵serverId
	 * @return
	 */
	public static Map<Integer, Integer> getServerMap(int dayNum) {
		Map<Integer, Integer> result = new HashMap<>();
		if (map.containsKey(dayNum) == false) {
			return result;
		}
		List<CrossBattleDayConfig> cfgList = map.get(dayNum);
		for (CrossBattleDayConfig crossBattleDayConfig : cfgList) {
			result.put(crossBattleDayConfig.getHomeServerId(), crossBattleDayConfig.getAwayServerId());
		}
		return result;
	}
	
	/**
	 * 判断天数和配置天数一致
	 * @param day
	 * @return
	 */
	public static boolean dayConfigSuccess(int day) {
		if (totalDay == day) {
			return true;
		}
		return false;
	}
	
	

}
