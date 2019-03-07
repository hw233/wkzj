package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 聚仙阵获取仙人星级配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "recruitStarConfig")
public class RecruitStarConfig implements ModelAdapter {

	/**
	 * 类型
	 */
	public int type;
	
	/**
	 * 星级
	 */
	public int star;
	
	/**
	 * 几率
	 */
	public int rate;
	
	/**
	 *  recruitType:类型：1：小，2：大
	 */
	public int recruitType;
	
	
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
}
