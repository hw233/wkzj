package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName="traderDiscountConfig")
public class TraderDiscountConfig implements ModelAdapter {
	
	/**
	 * id
	 */
	public int id;
	
	/**
	 * 折扣(千分比)
	 */
	public int discount;
	
	/**
	 * 几率
	 */
	public int rate;

	@Override
	public void initialize() {
		
	}

}
