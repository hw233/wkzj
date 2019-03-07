package com.jtang.worldserver.server.iohandler;

import java.io.IOException;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jiatang.common.type.WorldState;
import com.jtang.core.mina.router.Router;
import com.jtang.core.protocol.ActorRequest;
import com.jtang.core.protocol.ActorResponse;
import com.jtang.worldserver.component.World;
import com.jtang.worldserver.server.session.WorldSession;

@Component
public class WorldServerIoHandler implements IoHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldServerIoHandler.class);
	
	@Autowired
	@Qualifier("WorldRouterImpl")
	private Router router;
	
	@Autowired
	private WorldSession worldSession;
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if(message == null) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.error("message type error, packet is droped.");	
			}
			return;
		}

		ActorRequest request = (ActorRequest) message;		
		if (LOGGER.isDebugEnabled()) {				
			long startTime = System.currentTimeMillis();
			router.forward(session, request);// 分派到各模块的方法
			long endTime = System.currentTimeMillis();	
			LOGGER.debug(String.format("[messageReceived] session id:[%d] module:[%d] cmd:[%d] request time:[%d]ms", 
					session.getId(), request.getModule(), request.getCmd(), endTime - startTime));
		} else {
			router.forward(session, request);// 分派到各模块的方法
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			if ((message != null) && (message instanceof ActorResponse)) {
				ActorResponse response = (ActorResponse) message;
				byte module = response.getModule();
				byte cmd = response.getCmd();
				int byteLength = 0;
				if(response.getValue() != null) {
					byteLength = response.getValue().length;
				}
				LOGGER.debug(String.format("[messageSent] module: [%s] cmd: [%s] statuscode:[%s], length:[%d byte]", module, cmd,
						response.getStatusCode(), byteLength));
			}
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("[sessionClosed] sessionid:[%s] session close.", session == null ? 0 : session.getId()));
		}
		worldSession.remove(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		if (session == null) {
			return;
		}
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus idleStatus) throws Exception {
		if (idleStatus == IdleStatus.BOTH_IDLE) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("[sessionIdle] session:[%s] close. enter Idle status.", session));
			}
			worldSession.closeIoSession(session, false);
		}
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		//维护或关闭状态则禁止连接
		if (World.state == WorldState.MAINTAIN || World.state == WorldState.CLOSE) {
			worldSession.closeIoSession(session, false);
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
		
		//throwable class name
		String className = throwable.getClass().getName();
		
		// filter write to closed socket channel
		if (throwable instanceof WriteException) {
			LOGGER.error(String.format("[exceptionCaught] className:[%s] session:[%s]", className, session));
			return;
		}

		Throwable ex = throwable;
		StringBuilder builder = new StringBuilder();
		while (ex != null) {
			StackTraceElement[] stackTrace = ex.getStackTrace();
			for (StackTraceElement st : stackTrace) {
				builder.append("\t").append(st.toString()).append("\n");
			}
			if (ex == ex.getCause()) {
				break;
			} else {
				ex = ex.getCause();
				if (ex != null) {
					builder.append("CAUSE\n").append(ex.getMessage()).append(ex).append("\n");
				}
			}
		}

		// filter connection reset by peer exception ...
		if (throwable instanceof IOException && builder.indexOf("sun.nio.ch.SocketChannelImpl.read") > 0) {
			LOGGER.error(String.format("[exceptionCaught] session:[%s] className:[%s] msg:[%s]", session, className,throwable.getMessage()));
			return;
		}
		
		LOGGER.error(String.format("[exceptionCaught] session:[%s] className:[%s] error:[%s]", session, className, builder.toString()));
	}

}
