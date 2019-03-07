package com.jtang.gameserver.server.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jtang.core.mina.router.Router;
import com.jtang.core.mina.router.RouterHandler;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 模块处理句柄基类,所有接收消息类都必需继承于此
 * 
 * @author 0x737263
 * 
 */
public abstract class GatewayRouterHandlerImpl extends RouterHandler {

	@Autowired
	protected PlayerSession playerSession;

	@Autowired
	@Qualifier("GatewayRouterImpl")
	private Router router;
	
	@Override
	public void register() {
		router.register(this);
	}
}