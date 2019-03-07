package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
/**
 * 聚仙阵掉落配置
 * @author ludd
 *
 */
@DataFile(fileName = "recruitDropConfig")
public class RecruitDropConfig implements ModelAdapter{
	/**
	 * 类型（1：装备，2：仙人魂魄 ，3：仙人,4:装备碎片）
	 */
	 public int type;
	 /**
	  * 星级
	  */
	 public int star;
	 /**
	  * 装备或者仙人id
	  */
	 public int id;
	 
	 /**
	  * 概率千分比
	  */
	 public int rate;
	 
	 /**
	  * 数量
	  */
	 public int num;
	 
	 /**
	  * 类型：1:小，2：大
	  */
	 public int recruitType;
	 
	@Override
	public void initialize() {
		
	}

}
