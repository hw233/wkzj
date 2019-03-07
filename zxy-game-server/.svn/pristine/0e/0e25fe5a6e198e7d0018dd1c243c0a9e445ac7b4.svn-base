package com.jtang.gameserver.server.router;

import java.lang.reflect.Method;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.Router;
import com.jtang.core.mina.router.RouterHandler;
import com.jtang.core.protocol.DataPacket;

/**
 * 
 * @author 0x737263
 *
 */
@Component("AdminRouterImpl")
public class AdminRouterImpl extends Router {

	@Override
	public void forward(IoSession session, DataPacket dataPacket) {
		if (forwardValidate(session, dataPacket) == false) {
			return;
		}

		try {
			RouterHandler handler = MODULE_MAPS.get(dataPacket.getModule());
			Method method = handler.getMethod(dataPacket.getCmd());
			method.invoke(handler, session, dataPacket.getValue(), dataPacket.convert2Response());
		} catch (Exception ex) {
			LOGGER.error("forward", ex);
		}
	}
}