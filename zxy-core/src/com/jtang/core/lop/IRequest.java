package com.jtang.core.lop;

import java.util.Map;

/**
 * lop请求接口
 * @author 0x737263
 *
 */
public interface IRequest {

	/**
	 * 模块名称
	 * @return
	 */
	String moduleName();

	/**
	 * 调用名称
	 * @return
	 */
	String invokeName();

	/**
	 * 请求参数
	 * @return
	 */
	Map<String, String> getParameters();
}
