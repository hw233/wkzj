package com.jtang.gameserver.server.iohandler;

import static com.jtang.core.protocol.StatusCode.MAINTAIN_SERVER;

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

import com.jtang.core.mina.router.Router;
import com.jtang.core.protocol.Request;
import com.jtang.core.protocol.Response;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.component.listener.ListenerFacade;
import com.jtang.gameserver.server.firewall.Firewall;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 收发包主要处理类
 * 
 * @author 0x737263
 * 
 */
@Component
public class GateServerIoHandler implements IoHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GateServerIoHandler.class);

	@Autowired
	private Firewall firewall;

	@Autowired
	@Qualifier("GatewayRouterImpl")
	private Router router;

	@Autowired
	private PlayerSession playerSession;
	
	@Autowired
	private ListenerFacade listenerFacade;

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if(message == null) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.error("message type error, packet is droped.");	
			}
			return;
		}

		Request request = (Request) message;
		
		if (LOGGER.isDebugEnabled()) {				
			long startTime = System.currentTimeMillis();
			router.forward(session, request);// 分派到各模块的方法
			long endTime = System.currentTimeMillis();	
			Long actorId = playerSession.getActorId(session);
			LOGGER.debug(String.format("[messageReceived] sessionId:[%s] actorId:[%d] module:[%d] cmd:[%d] request time:[%d]ms", session.getId(),
					actorId, request.getModule(), request.getCmd(), endTime - startTime));
		} else {
			router.forward(session, request);// 分派到各模块的方法
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			if ((message != null) && (message instanceof Response)) {
				Response response = (Response) message;
				byte module = response.getModule();
				byte cmd = response.getCmd();
				int byteLength = 0;
				if(response.getValue() != null) {
					byteLength = response.getValue().length;
				}
				Long actorId = playerSession.getActorId(session);
				LOGGER.debug(String.format("[messageSent] sessionId:[%s] actorId:[%s] module:[%s] cmd:[%s] statuscode:[%s], length:[%d byte]",
						session.getId(), actorId, module, cmd, response.getStatusCode(), byteLength));
			}
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		Long actorId = playerSession.getActorId(session);
		
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("[sessionClosed] sessionId:[%s] actorid:[%s] session close.", session.getId(), actorId));
			}
			
			if (actorId != null && actorId > 0L) {
				//添加登出事件
				listenerFacade.addLogoutListener(actorId);
			}
		} catch (Exception ex) {
			LOGGER.error("{sessionClosed}", ex);
		}

		firewall.removeBlockCounter(session);
		playerSession.removeFromOnlineList(session);
		playerSession.removeFromAnonymousList(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("[sessionCreated] sessionId:[%s]", session.getId()));
		}
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus idleStatus) throws Exception {
		if (idleStatus == IdleStatus.BOTH_IDLE) {
			playerSession.closeIoSession(session, false);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("[sessionIdle] sessionId:[%s] close. enter Idle status.", session.getId()));
			}
		}
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("[sessionOpened] sessionId:[%s] is opened.", session.getId()));
		}
		
		String ip = playerSession.getRemoteIp(session);
		if (Game.checkAllowIP(ip) == false) {
			playerSession.writeStatusCode(session, MAINTAIN_SERVER, true);
			return;
		}
		
		playerSession.put2AnonymousList(session);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
		long actorId = playerSession.getActorId(session);
		if (session != null) {
			playerSession.closeIoSession(session, false);
		}
		
		//throwable class name
		//String className = throwable.getClass().getName();
		
		// filter write to closed socket channel
		if (throwable instanceof WriteException) {
//			LOGGER.error(String.format("[exceptionCaught] sessionId:[%s] actorId:[%s] className:[%s]", session.getId(), actorId, className));
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
//			LOGGER.error(String.format("[exceptionCaught] sessionId:[%s] actorId:[%s] className:[%s] msg:[%s]", session.getId(), actorId, className,
//					throwable.getMessage()));
			return;
		}
		
		LOGGER.error(String.format("[exceptionCaught] sessionId:[%s] actorId:[%s] error:[%s]", session.getId(), actorId, builder.toString()));
	}
	
}