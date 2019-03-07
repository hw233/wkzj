package com.jtang.worldserver.dataconfig.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.DateUtils;
import com.jtang.worldserver.dataconfig.model.CrossBattleConfig;
import com.jtang.worldserver.dataconfig.model.CrossBattleExchangeConfig;

@Component
public class CrossBattleService extends ServiceAdapter {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private static CrossBattleConfig config;

	private static Map<Integer, CrossBattleExchangeConfig> CROSS_BATTLE_EXCHANGE_MAP = new HashMap<>();
	
	@Override
	public void clear() {
		CROSS_BATTLE_EXCHANGE_MAP.clear();
	}

	@Override
	public void initialize() {
		List<CrossBattleConfig> list = dataConfig.listAll(this,
				CrossBattleConfig.class);
		for (CrossBattleConfig crossBattleConfig : list) {
			config = crossBattleConfig;

			if (config.getSignupTime().after(config.getStartTime())) {
				LOGGER.error("报名时间与开始时间不正确");
			}
		}

		List<CrossBattleExchangeConfig> exchangeConfigList = dataConfig
				.listAll(this, CrossBattleExchangeConfig.class);
		for (CrossBattleExchangeConfig exchangeConfig : exchangeConfigList) {
			CROSS_BATTLE_EXCHANGE_MAP.put(exchangeConfig.getExchangeAwardId(),
					exchangeConfig);
		}
	}

	/**
	 * 是否在开放
	 * 
	 * @return
	 */
	public static boolean isOpen() {
		Date now = new Date();
		if (now.after(config.getStartDate()) && now.before(config.getEndDate())) {
			return true;
		}
		return false;
	}
	public static boolean isOpenDate() {
		Date now = new Date();
		Date start = DateUtils.string2Date(config.startDate, "yyyy-MM-dd");
		Date end = DateUtils.string2Date(config.endDate, "yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(end);
		c.add(Calendar.DAY_OF_MONTH, 1);
		end = c.getTime();
		if (now.after(start) && now.before(end)) {
			return true;
		}
		return false;
	}

	public static boolean isOpenTime() {
		Date now = new Date();
		return now.after(config.getStartTime())
				&& now.before(config.getEndTime());
	}

	/**
	 * 获取报名时间
	 * 
	 * @return
	 */
	public static int getSignupTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(config.getSignupTime());
		Long time = c.getTimeInMillis() / 1000;
		return time.intValue();
	}

	public static boolean signupStart() {
		Date now = new Date();
		return now.after(config.getSignupTime()) && now.before(config.getEndTime());
	}

	public static CrossBattleConfig get() {
		return config;
	}

	/**
	 * 获取兑换配置
	 */
	public static CrossBattleExchangeConfig getExchangeConfig(
			int exchangeAwardId) {
		return CROSS_BATTLE_EXCHANGE_MAP.get(exchangeAwardId);
	}

	/**
	 * 获取联赛第几天
	 * 
	 * @return
	 */
	public static int getDayNum() {
		int dayNum = DateUtils.getRemainDays(config.getStartDate(), new Date()) + 1;
//		int result = dayNum % CrossBattleDayService.getTotalDay() + 1;
		return dayNum;
	}
	
	
	public static int joinActorLevel() {
		return config.getJoinLevel();
	}

}
