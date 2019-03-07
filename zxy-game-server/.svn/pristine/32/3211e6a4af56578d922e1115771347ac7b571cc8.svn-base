package com.jtang.gameserver.worldclient.iohandler;

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

import com.jiatang.common.ModuleName;
import com.jiatang.common.baseworld.BaseWorldCmd;
import com.jiatang.common.baseworld.request.SessionRegisterG2W;
import com.jtang.core.mina.router.Router;
import com.jtang.core.protocol.ActorRequest;
import com.jtang.core.protocol.ActorResponse;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.worldclient.session.WorldClientSession;

@Component
public class WorldClientIoHandler implements IoHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldClientIoHandler.class);

	@Autowired
	@Qualifier("WorldClientRouterImpl")
	private Router router;
	
	@Autowired
	private WorldClientSession clientSession;

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if (message == null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error("message type error, packet is droped.");
			}
			return;
		}

		ActorResponse response = (ActorResponse) message;		
		if (LOGGER.isDebugEnabled()) {
			long startTime = System.currentTimeMillis();
			router.forward(session, response);// 分派到各模块的方法
			long endTime = System.currentTimeMillis();	
			LOGGER.debug(String.format("world client[messageReceived] session id:[%d] module:[%d] cmd:[%d] request time:[%d]ms", 
					session.getId(), response.getModule(), response.getCmd(), endTime - startTime));
		} else {
			router.forward(session, response);// 分派到各模块的方法
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			if ((message != null) && (message instanceof ActorRequest)) {
				ActorRequest request = (ActorRequest) message;
				byte module = request.getModule();
				byte cmd = request.getCmd();
				int byteLength = 0;
				if(request.getValue() != null) {
					byteLength = request.getValue().length;
				}
				LOGGER.debug(String.format("world client[messageSent] module: [%s] cmd: [%s]  length:[%d byte]", module, cmd,
						 byteLength));
			}
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("world client[sessionClosed] sessionid:[%s] session close.", session == null ? 0 : session.getId()));
		}
		clientSession.remove(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		if (session == null) {
			return; 
		}
		
		//添加加到本地客户端session管理
		clientSession.put(session);
		//向worldserver注册该session
		SessionRegisterG2W data = new SessionRegisterG2W(Game.getServerId());
		ActorRequest request = ActorRequest.valueOf(ModuleName.BASE, BaseWorldCmd.REGISTER, data.getBytes());
		clientSession.sendMsg(request);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus idleStatus) throws Exception {
		if (idleStatus == IdleStatus.BOTH_IDLE) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("world client[sessionIdle] world session:[%s] close. enter Idle status.", session));
			}
			clientSession.closeIoSession(session, true);
		}
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
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