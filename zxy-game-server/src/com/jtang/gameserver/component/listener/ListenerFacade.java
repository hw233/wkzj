package com.jtang.gameserver.component.listener;

/**
 * 对外监听统一处理接口
 * @author 0x737263
 *
 */
public interface ListenerFacade {

	/**
	 * 添加登陆监听
	 * @param actorId
	 */
	void addLoginListener(long actorId);
	
	/**
	 * 添加退出监听
	 * @param actorId
	 */
	void addLogoutListener(long actorId);
	
}
