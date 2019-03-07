package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.rhino.FormulaHelper;

@DataFile(fileName="loveMonsterFlushConfig")
public class LoveMonsterFlushConfig implements ModelAdapter {
	
	/**
	 * 刷新次数
	 */
	public int flushNum;
	
	/**
	 * 消耗的点券
	 */
	private String costTicket;
	
	public int getCostTicket(int flushNum){
		return FormulaHelper.executeCeilInt(costTicket, flushNum);
	}

	@Override
	public void initialize() {
		
	}

}
