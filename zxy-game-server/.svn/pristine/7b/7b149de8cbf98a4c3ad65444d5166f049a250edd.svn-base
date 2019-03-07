package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.DemonGlobalConfig;
import com.jtang.gameserver.module.demon.model.OpenTime;

/**
 * 集众降魔全局配置服务
 * @author ludd
 *
 */
@Component
public class DemonGlobalService extends ServiceAdapter {
	
	private static List<OpenTime> openTimes = new ArrayList<>();
	
	private static int defaultRankDiffcult = 1;
	private static int rankNum = 10;
	
	private static List<Integer> exchangeWeek = new ArrayList<>();
	private static List<OpenTime> exchangeTime = new ArrayList<>();
	
	private static Date openDate;
	
	private static int actorLevel;
	@Override
	public void clear() {
		openTimes.clear();
		exchangeWeek.clear();
		exchangeTime.clear();
	}
	
	@Override
	public void initialize() {
		List<DemonGlobalConfig> list = dataConfig.listAll(this, DemonGlobalConfig.class);
		for (DemonGlobalConfig demonConfig : list) {
			List<String[]> timeList = StringUtils.delimiterString2Array(demonConfig.getOpenTimes());
			for (String[] timestr : timeList) {
				OpenTime openTime = new OpenTime(timestr);
				openTimes.add(openTime);
			}
			rankNum = demonConfig.getRankNum();
			defaultRankDiffcult = demonConfig.getDefaultRankDifficult();
			
			String[] exchangeTimes = StringUtils.split(demonConfig.getExchangeTime(), Splitable.ELEMENT_SPLIT);
			for (String string : exchangeTimes) {
				String[] strs = StringUtils.split(string, Splitable.ATTRIBUTE_SPLIT);
				OpenTime o = new OpenTime(strs);
				exchangeTime.add(o);
			}
			
			exchangeWeek = StringUtils.delimiterString2IntList(demonConfig.getExchangeWeek(), Splitable.ATTRIBUTE_SPLIT);
			openDate = DateUtils.string2Date(demonConfig.getOpenDate(), "yyyy-MM-dd");
			actorLevel = demonConfig.getActorLevel();
			break;
		}
	}
	
	public static boolean inOpenDate() {
		if (openDate != null && openDate.before(new Date())){
			return true;
		}
		return false;
	}
	
	public static List<OpenTime> getOpenTimes() {
		return openTimes;
	}
	
	public static int getDefaultRankDiffcult() {
		return defaultRankDiffcult;
	}
	
	public static int getRankNum() {
		return rankNum;
	}
	
	
	public static boolean enableExchange() {
		Calendar c = Calendar.getInstance();
		int a = c.get(Calendar.DAY_OF_WEEK) - 1;
		a = a == 0 ? 7 : a;
		if (exchangeWeek.contains(a) == false) {
			return false;
		}
		for (OpenTime openTime : exchangeTime) {
			
			int nowHour = c.get(Calendar.HOUR_OF_DAY);
			int nowMinuts = c.get(Calendar.MINUTE);
			
			if (openTime.startH <= nowHour && nowHour <= openTime.endH && openTime.startM <= nowMinuts && nowMinuts <= openTime.endM) {
				return true;
			}
		}
		return false;
	}
	
	public static Date getOpenDate() {
		return openDate;
	}
	
	public static List<OpenTime> getExchangeTime() {
		return exchangeTime;
	}
	
	public static List<Integer> getExchangeWeek() {
		return exchangeWeek;
	}
	
	public static int getActorLevel() {
		return actorLevel;
	}
	
	
	


}
