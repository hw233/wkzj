package com.jtang.gameserver.dataconfig.model;

import java.util.Date;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.DatePattern;
import com.jtang.core.utility.DateUtils;
/**
 * 天神下凡仙人配置
 * @author lig
 *
 */
@DataFile(fileName = "deityDescendGlobalConfig")
public class DeityDescendGlobalConfig implements ModelAdapter {
	
	/**
	 * 活动开启时间
	 */
	private String startDate;
	
	/**
	 * 活动结束时间
	 */
	private String endDate;
	
	/**
	 * 当前显示仙人
	 */
	public int currentHeroId;
	
	@FieldIgnore
	public Date startTime;

	@FieldIgnore
	public Date endTime;
	
	@Override
	public void initialize() {
		startTime = DateUtils.string2Date(startDate, DatePattern.PATTERN_NORMAL);
		endTime = DateUtils.string2Date(endDate, DatePattern.PATTERN_NORMAL);
	}
	
	
}
