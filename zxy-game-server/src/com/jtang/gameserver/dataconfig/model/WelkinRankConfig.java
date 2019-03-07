package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "welkinRankConfig")
public class WelkinRankConfig implements ModelAdapter {

	/**
	 * 排名
	 */
	public int rank;
	
	/**
	 * 使用次数
	 */
	public int useNum;
	
	/**
	 * 奖励类型
	 */
	public int type;
	
	/**
	 * 奖励id
	 */
	public int id;
	
	/**
	 * 奖励数量
	 */
	public int num;
	
	@Override
	public void initialize() {
		
	}

}
