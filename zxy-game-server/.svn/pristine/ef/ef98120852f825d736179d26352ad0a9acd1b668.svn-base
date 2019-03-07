package com.jtang.gameserver.dataconfig.model;

import java.util.Date;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.DateUtils;

@DataFile(fileName="basinConfig")
public class BasinConfig implements ModelAdapter {
	
	/**
	 * 开始时间
	 */
	private String startTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
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
