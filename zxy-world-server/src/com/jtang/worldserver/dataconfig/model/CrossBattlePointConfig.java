package com.jtang.worldserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 最强势力排名相关配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "crossBattlePointConfig")
public class CrossBattlePointConfig implements ModelAdapter {

	/**
	 * 伤害排名
	 */
	private int hurtRank;
	
	/**
	 * 获得的贡献点
	 */
	private int exchangePoint;
	
	@Override
	public void initialize() {
	}

	public int getExchangePoint() {
		return exchangePoint;
	}
	
	public int getHurtRank() {
		return hurtRank;
	}

}
