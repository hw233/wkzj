package com.jtang.gameserver.worldclient.router;

import java.lang.reflect.Method;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.Router;
import com.jtang.core.mina.router.RouterHandler;
import com.jtang.core.protocol.ActorResponse;
import com.jtang.core.protocol.DataPacket;

@Component("WorldClientRouterImpl")
public class WorldClientRouterImpl extends Router {

	@Override
	public void forward(IoSession session, DataPacket dataPacket) {
		if (forwardValidate(session, dataPacket) == false) {
			return;
		}

		try {
			RouterHandler handler = MODULE_MAPS.get(dataPacket.getModule());
			Method method = handler.getMethod(dataPacket.getCmd());
			// 转换为 ActorResponse
			method.invoke(handler, session, (ActorResponse) dataPacket);
		} catch (Exception ex) {
			LOGGER.error("forward", ex);
		}
	}
}
