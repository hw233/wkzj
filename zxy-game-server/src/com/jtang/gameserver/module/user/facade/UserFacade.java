package com.jtang.gameserver.module.user.facade;

import org.apache.mina.core.session.IoSession;

/**
 * 用户相关的facade
 * @author 0x737263
 *
 */
public interface UserFacade {

	/**
	 * 加入到用户登陆队列
	 */
	void putUserLoginQueue(IoSession session, int platformId, String token);

}
