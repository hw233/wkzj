package com.jtang.gameserver.server.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jtang.core.mina.router.Router;
import com.jtang.core.mina.router.RouterHandler;

/**
 * 管理接口处理抽象类
 * @author 0x737263
 *
 */
public abstract class AdminRouterHandlerImpl extends RouterHandler {
	@Autowired
	@Qualifier("AdminRouterImpl")
	private Router router;

	@Override
	public void register() {
		router.register(this);
	}
}