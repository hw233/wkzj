package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.rhino.FormulaHelper;

@DataFile(fileName = "storyFightConfig")
public class StoryFightConfig implements ModelAdapter{

	/**
	 * 消耗点券(公式,x1代表使用次数)
	 */
	private String costTicket;
	
	/**
	 * 扫荡符id
	 */
	public int goodsId;
	
	/**
	 * 购买扫荡符的数量
	 */
	public int num;
	
	@Override
	public void initialize() {
		
	}

	public int getCostTicket(int num){
		return FormulaHelper.executeCeilInt(costTicket, num);
	}
}
