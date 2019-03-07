package com.jtang.worldserver.server.router;

import java.lang.reflect.Method;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.Router;
import com.jtang.core.mina.router.RouterHandler;
import com.jtang.core.protocol.ActorRequest;
import com.jtang.core.protocol.DataPacket;
import com.jtang.worldserver.server.session.WorldSession;

@Component("WorldRouterImpl")
public class WorldRouterImpl extends Router {

	@Autowired
	WorldSession worldSession;
	
	@Override
	public void forward(IoSession session, DataPacket dataPacket) {
		if (forwardValidate(session, dataPacket) == false) {
			return;
		}

		try {
			RouterHandler handler = MODULE_MAPS.get(dataPacket.getModule());
			Method method = handler.getMethod(dataPacket.getCmd());
			int serverId = worldSession.getServerId(session);
			ActorRequest actorRequest = (ActorRequest) dataPacket;
			method.invoke(handler, session, serverId, actorRequest, actorRequest.convert2Response());
		} catch (Exception ex) {
			LOGGER.error("forward", ex);
		}
	}
}
