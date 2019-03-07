package com.jtang.core.dataconfig;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 服务适配器接口
 * 
 * @author 0x737263
 */
public abstract class ServiceAdapter {	
	
	@Autowired
	public DataConfig dataConfig;
	
	/**
	 * 重载initialize() 方法前会调用该方法清空
	 */
	public abstract void clear(); 
	
	/**
	 * 继承的服务类初始化函数
	 */
	public abstract void initialize();
}