package com.jtang.gameserver.dataconfig.model;

import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "randomRewardConfig")
public class RandomRewardConfig implements ModelAdapter {

	/**
	 * id
	 */
	public int id;
	
	/**
	 * 刷新间隔时间(秒)
	 */
	public String flushTime;
	
	/**
	 * 奖励类型
	 */
	public String rewardType;
	
	@FieldIgnore
	public int flushStartTime;
	
	@FieldIgnore
	public int flushEndTime;
	
	@FieldIgnore
	public Map<Integer,Integer> map;
	
	@Override
	public void initialize() {
		List<Integer> list = StringUtils.delimiterString2IntList(flushTime, Splitable.ATTRIBUTE_SPLIT);
		flushStartTime = list.get(0);
		flushEndTime = list.get(1);
		
		map = StringUtils.delimiterString2IntMap(rewardType);
		
		flushTime = null;
		rewardType = null;
	}

	public int getFlushTime() {
		return RandomUtils.nextInt(flushStartTime, flushEndTime);
	}

}
