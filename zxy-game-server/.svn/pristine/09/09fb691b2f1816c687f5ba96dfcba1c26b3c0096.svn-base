package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "moduleControlConfig")
public class ModuleControlConfig implements ModelAdapter {
	
	/**
	 * moduleId
	 */
	private int moduleId;
	
	/**
	 * 开启状态
	 * 0：未开启
	 * 1：开启
	 */
	private int open;
	
	public String desc;
	

	@Override
	public void initialize() {
		
	}
	
	public boolean isOpen() {
		return open == 1;
	}
	
	
	public int getModuleId() {
		return moduleId;
	}

}
