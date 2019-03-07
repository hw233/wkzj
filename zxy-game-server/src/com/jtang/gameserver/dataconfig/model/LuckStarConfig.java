package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "luckStarConfig")
public class LuckStarConfig implements ModelAdapter {

	/**
	 * 刷新间隔时间
	 */
	public int flushTime;
	
	/**
	 * 最大星数
	 */
	public int maxStar;
	
	/**
	 * 第一次使用完必出奖励
	 */
	public int mustRewardId;
	
	/**
	 * 开放等级
	 */
	public int openLevel;
	
	
	@Override
	public void initialize() {
		
	}

}