package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.ExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.dataconfig.model.TrialCaveGlobalConfig;
import com.jtang.gameserver.dataconfig.model.TrialCaveMonsterConfig;
import com.jtang.gameserver.module.user.model.VipPrivilege;

/**
 * 试炼洞service类
 * @author lig
 */
@Component
public class TrialCaveService extends ServiceAdapter {
	
	/**
	 * key = achieveId
	 * value = 
	 */
	private static List<TrialCaveGlobalConfig> TRIAL_CAVE_GLOBAL_CONFIG_LIST = new ArrayList<TrialCaveGlobalConfig>();
	/**
	 * key = achieveId
	 * value = 
	 */
	private static Map<Integer, TrialCaveMonsterConfig> TRIAL_CAVE_MONSTER_CONFIG_MAP = new HashMap<>();
	
	@Override
	public void clear() {
		TRIAL_CAVE_GLOBAL_CONFIG_LIST.clear();
		TRIAL_CAVE_MONSTER_CONFIG_MAP.clear();
	}
	
	
	@Override
	public void initialize() {
		TRIAL_CAVE_GLOBAL_CONFIG_LIST = dataConfig.listAll(this, TrialCaveGlobalConfig.class);
		
		List<TrialCaveMonsterConfig> allTrialCaveMonster = dataConfig.listAll(this, TrialCaveMonsterConfig.class);
		for (TrialCaveMonsterConfig config : allTrialCaveMonster) {
			if (!TRIAL_CAVE_MONSTER_CONFIG_MAP.containsKey(config.id)) {
				TRIAL_CAVE_MONSTER_CONFIG_MAP.put(config.id, config);
			}
		}
	}

	public static TrialCaveGlobalConfig getTrialCaveGlobalConfig() {
		return TRIAL_CAVE_GLOBAL_CONFIG_LIST.get(0);
	}

	public static int getOpenLv() {
		return getTrialCaveGlobalConfig().openLv;
	}

	public static int getIntervalTime() {
		return getTrialCaveGlobalConfig().intervalTime;
	}
	
	public static int getMapId(int entranceId) {
		if (entranceId == 1) {
			return getTrialCaveGlobalConfig().entrance1MapId;
		} else {
			return getTrialCaveGlobalConfig().entrance2MapId;
		}
	}
	
	public static Map<Integer, Integer> getMonsterLineupMap(int monsterLineupId) {
		return TRIAL_CAVE_MONSTER_CONFIG_MAP.get(monsterLineupId).getMonster();
	}

	public static int getMonsterLineupMorale(int monsterLineupId) {
		return TRIAL_CAVE_MONSTER_CONFIG_MAP.get(monsterLineupId).morale;
	}
	
	public static String getMonsterHPExpr() {
		return getTrialCaveGlobalConfig().hpExpr;
	}
	
	public static String getMonsterAttackExpr() {
		return getTrialCaveGlobalConfig().attackExpr;
	}
	
	public static String getMonsterDefenseExpr() {
		return getTrialCaveGlobalConfig().defenseExpr;
	}
	
	public static long getActivityStartDate(int dayOfWeek) {
		return TimeUtils.getThisWeekOnedayStart(dayOfWeek);
	}
	
	public static long getActivityEndDate(int dayOfWeek) {
		return TimeUtils.getThisWeekOnedayEnd(dayOfWeek);
	}
	
	/**
	 * 活动结束时间
	 * @return
	 */
	public static long getActivityEndTime() {
		List<Integer> dateList = getTrialCaveGlobalConfig().openDateList;
		int dayOfWeek = TimeUtils.getTodayDayOfWeek();
		int endDay = -1;
		for (int i = dayOfWeek; i <= 7; i++) {
			if (dateList.contains(i)) {
				endDay = i;
				break;
			}
		}
		Long fixDayEnd = TimeUtils.getThisWeekOnedayEnd(endDay);
		
		Long actEnd = getTrialCaveGlobalConfig().closeDateTime.getTime();
		long endTime = Math.max(fixDayEnd, actEnd);
		return endTime;
	}
	
	public static int getVIPTrialNumByVIPLevel(VipPrivilege vipPrivilege, int entranceId) {
		if (null == vipPrivilege) {
			if (entranceId == 1) {
				return getTrialCaveGlobalConfig().entrance1TrialNum;
			} else {
				return getTrialCaveGlobalConfig().entrance2TrialNum;
			}
		} else {
			if (entranceId == 1) {
				return vipPrivilege.VIPEntrance1TrialNum;
			} else {
				return vipPrivilege.VIPEntrance2TrialNum;
			}
		}
	}
	
	public static int getResetNumByVIPLevel(VipPrivilege vipPrivilege) {
		if (null == vipPrivilege) {
			return getTrialCaveGlobalConfig().resetTrialNum;
		} else {
			return vipPrivilege.VIPResetTrialNum;
		}
	}
	
	/**
	 * 根据重置次数获取重置的价格
	 * @param times
	 * @return
	 */
	public static int getResetCostByBuyTimes(int times) {
		return getTrialCaveGlobalConfig().resetTrialPriceList.get(times);
	}
	
	public static int randomMonsterLineupId() {
		int index = RandomUtils.nextInt(1, getTrialCaveGlobalConfig().lineupMonsterList.size());
		return index;
	}
	
	public static Map<Integer, MonsterVO> generateMonsters(Map<Integer, Integer> lineup) {
		Map<Integer, MonsterVO> monsterMap = new HashMap<Integer, MonsterVO>();
		for (Map.Entry<Integer, Integer> entity : lineup.entrySet()) {
			int monsterId = entity.getValue();
			int index = entity.getKey();
			//构建怪物实体
			if (monsterId != -1) {
				MonsterVO monster = new MonsterVO(MonsterService.get(monsterId));
				monsterMap.put(index, monster);
			}
		}
		return monsterMap;
	}
	
	/**
	 * 是否在指定日期内
	 */
	public static boolean isInSpecifiedDay() {
		boolean isSpecifiedPeriod = false;
		Long startDate = getTrialCaveGlobalConfig().openDateTime.getTime();
		Long endDate = getTrialCaveGlobalConfig().closeDateTime.getTime();
		Long now = System.currentTimeMillis();
		if (TimeUtils.isInSpecifiedPeriodOfTime(now.longValue(), startDate, endDate)) {
			isSpecifiedPeriod = true;
		}
		boolean isFixedPeriod = false;
		List<Integer> dates = getTrialCaveGlobalConfig().openDateList;
		for (Integer day : dates) {
			long dayStart = TrialCaveService.getActivityStartDate(day.intValue());
			long dayEnd = TrialCaveService.getActivityEndDate(day.intValue());
			if (TimeUtils.isInSpecifiedPeriodOfTime(now.longValue(), dayStart, dayEnd)) {
				isFixedPeriod = true;
			}
		}
		boolean result = isSpecifiedPeriod || isFixedPeriod;
		return result;
	}
	
	public static List<RewardObject> getRewardList(boolean battleResult, int entranceId, int level, int trialCount){
		List<RewardObject> list = new ArrayList<>();
		List<ExprRewardObject> selfAwardGoods = null;
		if (entranceId == 1) {
			selfAwardGoods = TrialCaveService.getTrialCaveGlobalConfig().entrance1RewardList;
		} else {
			selfAwardGoods = TrialCaveService.getTrialCaveGlobalConfig().entrance2RewardList;
		}
		boolean isInActivityTime = false;
		int activityRatio = 1;
		
		if (TrialCaveService.isInSpecifiedDay()) {
			isInActivityTime = true;
			activityRatio = TrialCaveService.getTrialCaveGlobalConfig().activityRatio;
		}
		for(ExprRewardObject obj : selfAwardGoods){
			RewardObject reward = obj.clone(level);
			reward.num = reward.num * activityRatio;
			if (!battleResult){
				reward.num = reward.num * TrialCaveService.getTrialCaveGlobalConfig().failRewardPrecent / 100;
			}
			list.add(reward);
		}
		if (isInActivityTime) {
			if (trialCount > TrialCaveService.getTrialCaveGlobalConfig().extraRewardTimes) {
				return list;
			}
			for(ExprRewardObject obj : TrialCaveService.getTrialCaveGlobalConfig().extraRewardList){
				RewardObject reward = obj.clone(level);
				list.add(reward);
			}
		}
		return list;
	}
}
