package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName="boxGoodsConfig")
public class BoxGoodsConfig implements ModelAdapter {

	/**
	 * 宝箱id
	 */
	public int boxId;
	
	/**
	 * 奖励id
	 */
	public int id;
	
	/**
	 * 数量
	 * [公式<x1表示玩家等级>/数量]
	 */
	public String num;
	
	/**
	 * 几率
	 */
	public int proportion;
	
	@Override
	public void initialize() {
		
	}

}
