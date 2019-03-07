package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.rhino.FormulaHelper;

@DataFile(fileName="powerShopFlushConfig")
public class PowerShopFlushConfig implements ModelAdapter {
	
	/**
	 * 刷新次数
	 */
	public int flushNum;
	
	/**
	 * 消耗的点券
	 */
	private String costGoods;
	
	public int getCostGoods(int flushNum){
		return FormulaHelper.executeCeilInt(costGoods, flushNum);
	}

	@Override
	public void initialize() {
		
	}

}
