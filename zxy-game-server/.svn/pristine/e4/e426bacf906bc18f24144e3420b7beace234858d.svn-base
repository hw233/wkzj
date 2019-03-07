package com.jtang.gameserver.dataconfig.model;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName="traderPoolConfig")
public class TraderPoolConfig implements ModelAdapter {
	
	/**
	 * id
	 */
	public int id;
	
	/**
	 * 奖励组成(类型<RewardType>_数量|...)
	 */
	public String rewardPool;
	
	/**
	 * 几率
	 */
	public int rate;
	
	/**
	 * key类型
	 * value数量
	 */
	@FieldIgnore
	public Map<Integer,Integer> typeMap = new HashMap<>();

	@Override
	public void initialize() {
		typeMap = StringUtils.delimiterString2IntMap(rewardPool);
		rewardPool = null;
	}

}
