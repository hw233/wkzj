package com.jtang.core.dataconfig.parse;

import java.io.InputStream;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;

/**
 * 数据解析接口
 * 
 * @author 0x737263
 * 
 */
public interface DataParser {

	/**
	 * 读取配置文件后进行解析
	 * 
	 * @param stream  文件流
	 * @param className 解析映射类文件
	 * @return
	 */
	public <T extends ModelAdapter> List<T> parse(InputStream stream, Class<T> className);
}
