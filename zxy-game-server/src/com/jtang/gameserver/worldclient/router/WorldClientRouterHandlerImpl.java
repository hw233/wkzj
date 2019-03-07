package com.jtang.gameserver.worldclient.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.Router;
import com.jtang.core.mina.router.RouterHandler;

/**
 * 世界服接口处理抽象类
 * @author 0x737263
 *
 */
@Component
public abstract class WorldClientRouterHandlerImpl extends RouterHandler {

	@Autowired
	@Qualifier("WorldClientRouterImpl")
	private Router router;

	@Override
	public void register() {
		router.register(this);
	}
}