package com.jtang.gameserver.module.user.platform;

import java.util.HashMap;
import java.util.Map;

/**
 * 平台登陆返回结果类
 * @author 0x737263
 *
 */
public class PlatformLoginResult {
	
	/**
	 * 用户唯一标识
	 */
	public String uid = "";
	
	/**
	 * 登陆后的一些扩展参数 
	 */
	public Map<String,String> params = new HashMap<>();
	
	
	public static PlatformLoginResult valueOf(String uid) {
		PlatformLoginResult result = new PlatformLoginResult();
		result.uid = uid;
		return result;
	}
	
	public void addParams(String key, String value) {
		params.put(key, value);
	}
	
}
