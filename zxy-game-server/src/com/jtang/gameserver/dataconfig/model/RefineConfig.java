package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 精炼室配置
 * @author liujian
 *
 */
@DataFile(fileName = "refineConfig")
public class RefineConfig implements ModelAdapter {
	
	/**
	 * 精炼类型
	 */
	private int type;
	
	/**
	 * 增加属性
	 */
	private int addValue;
	
	
	@Override
	public void initialize() {		
	}

	public int getType(){
		return type;
	}

	public float getAddValue() {
		return addValue / 100.0f;
	}


}
