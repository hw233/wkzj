package com.jtang.worldserver.server.router;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jtang.core.mina.router.Router;
import com.jtang.core.mina.router.RouterHandler;
import com.jtang.core.protocol.ActorResponse;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.protocol.StatusCode;
import com.jtang.worldserver.server.session.WorldSession;

public abstract class WorldRouterHandlerImpl extends RouterHandler {

	@Autowired
	@Qualifier("WorldRouterImpl")
	protected Router router;
	

	@Autowired
	protected WorldSession worldSession;
	
	@Override
	public void register() {
		router.register(this);
	}
	
	public void sessionWrite(IoSession session, ActorResponse response, IoBufferSerializer buffer, short statusCode) {
		if (statusCode == StatusCode.SUCCESS) {
			response.setValue(buffer.getBytes());
			session.write(response);
		} else {
			response.setStatusCode(statusCode);
			session.write(response);
		}
	}

}
