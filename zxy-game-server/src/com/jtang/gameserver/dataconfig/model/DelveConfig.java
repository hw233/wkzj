package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 潜修房配置
 * @author ludd
 *
 */
@DataFile(fileName = "delveConfig")
public class DelveConfig implements ModelAdapter {

	/**
	 *  潜修类型
	 */
	 public int type;     
	 
	 /**
	  *  提升属性的个数
	  */
	 public int upPropNum;
	 
	 /**
	  * 潜修石ID
	  */
	 public int goodsId;
	 
	 /**
	  * 该等级的潜修室为仙人潜修提升的属性的加成百分比
	  */
	 public int proportion;
	 
	 /**
	  * 是否可重修
	  */
	 private int ableRepeatDelve;
	 
	@Override
	public void initialize() {
	}
	
	
	public float getProportion(){
		return proportion / 100.0f;
	}

	public boolean getAbleRepeatDelve(){
		return ableRepeatDelve > 0 ? true : false;
	}
}
