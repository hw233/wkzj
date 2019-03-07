package com.jtang.core.dataconfig;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * 数据配置接口 
 * @author 0x737263
 *
 */
public interface DataConfig {

	/**
	 * 根据类名获取数据配置列表
	 * @param invokeClazz	调用者的Service类
	 * @param modelClass	需要获取的Model类
	 * @return
	 */
	abstract <T extends ModelAdapter> List<T> listAll(ServiceAdapter invokeClazz, Class<T> modelClass);

	/**
	 * 重载配置文件
	 * @param fileName	文件名
	 * @return
	 */
	boolean reload(String fileName);
	
	/**
	 * 重载配置文件
	 * @param fileName	文件名
	 * @param newData	新的文件流
	 * @return
	 */
	boolean reload(String fileName, URL url);
	
	/**
	 * 获取所有配置文件名
	 */
	Set<String> getAllConfigName();

	/**
	 * 校验配置文件流
	 * @param name
	 * @param inputStream
	 * @return
	 */
	boolean checkModelAdapter(String name, InputStream inputStream);
}
