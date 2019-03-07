package com.jtang.worldserver.module.base.facade;

import com.jtang.core.result.Result;

public interface BaseFacade {

	/**
	 * 关闭服务器
	 */
	Result shutdownServer();
	
	/**
	 * 热刷配置文件
	 * @param fileName
	 * @param data
	 * @return
	 */
	Result reloadConfig(String fileName, String data);

}
