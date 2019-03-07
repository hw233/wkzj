package com.jtang.gameserver.module.user.platform;

import com.jtang.core.result.TResult;

/**
 * 平台接口调用
 * @author 0x737263
 *
 */
public interface PlatformInvoke {
	

	/**
	 * 执行效验 
	 * @param params	 根据platformId，对应的token格式各不相同
	 * @return
	 */
	public TResult<PlatformLoginResult> login(Integer platformId, String params);
}
