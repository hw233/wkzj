package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "sysmailConfig")
public class SysmailConfig implements ModelAdapter {

	/**
	 * 配置id
	 */
	public int id;
	
	/**
	 * 文本内容
	 */
	public String text;
	
	@Override
	public void initialize() {
		
	}

}
