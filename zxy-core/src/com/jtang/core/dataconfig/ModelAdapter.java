package com.jtang.core.dataconfig;

/**
 * model包中的类继承于此
 * resource.dataconfig包中的文件名必需和model类名一样(忽略大小写)
 * @author 0x737263
 */
public interface ModelAdapter {

	/**
	 * 初始化处理方法(用于model初始化时做一些自定义处理)
	 */
	public abstract void initialize();

}
