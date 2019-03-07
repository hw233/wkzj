package com.jtang.gameserver.dataconfig.model;

import java.util.Date;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.DateUtils;

@DataFile(fileName = "welkinGlobalConfig")
public class WelkinGlobalConfig implements ModelAdapter {

	/**
	 * 开始时间
	 */
	public String startTime;
	
	/**
	 * 结束时间
	 */
	public String endTime;
	
	/**
	 * 使用金币次数
	 */
	public int useGoldCount;
	
	/**
	 * 每次消耗多少金币
	 */
	public int goldNum;
	
	/**
	 * 上榜最低使用次数
	 */
	public int rankCount;
	
	/**
	 * 排行榜只取前多少名
	 */
	public int rank;
	
	/**
	 * 排行榜最低发奖名次
	 */
	public int rankReward;
	
	@FieldIgnore
	public int start;
	
	@FieldIgnore
	public int end;
	
	@Override
	public void initialize() {
		Date dateStart = DateUtils.string2Date(startTime, "yyyy-MM-dd HH:mm:ss");
		Long ls = dateStart.getTime() / 1000;
		start = ls.intValue();
		Date dateEnd = DateUtils.string2Date(endTime, "yyyy-MM-dd HH:mm:ss");
		Long le = dateEnd.getTime() / 1000;
		end = le.intValue();
		startTime = null;
		endTime = null;
	}

}
