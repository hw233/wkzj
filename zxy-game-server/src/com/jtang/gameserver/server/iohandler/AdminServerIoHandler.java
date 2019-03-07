package com.jtang.gameserver.server.iohandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.Router;
import com.jtang.core.protocol.Request;
import com.jtang.core.protocol.Response;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.server.session.AdminSession;

/**
 * 
 * @author 0x737263
 *
 */
@Component
public class AdminServerIoHandler implements IoHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminServerIoHandler.class);

	@Autowired
	@Qualifier("AdminRouterImpl")
	private Router router;
	
	@Autowired
	private AdminSession adminSession;

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
			LOGGER.debug(String.format("[messageReceived] session id:[%d] module:[%d] cmd:[%d] request time:[%d]ms", 
					session.getId(), request.getModule(), request.getCmd(), endTime - startTime));
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
		adminSession.remove(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		if (session == null) {
			return;
		}
		String remoteIp = null;
		SocketAddress add = session.getRemoteAddress();
		if(add != null) {
			remoteIp = ((InetSocketAddress) add).getAddress().getHostAddress();
		}
		if (remoteIp == null) { // StringUtils.isBlank(remoteIp)
			remoteIp = ((InetSocketAddress) session.getLocalAddress()).getAddress().getHostAddress();
		}
		if (!Game.checkAdminIP(remoteIp)){
			LOGGER.info(String.format("当前访问IP:[%s]，禁止访问！", remoteIp));
			session.close(true);
			return;
		} 
		
		adminSession.put(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus idleStatus) throws Exception {
		if (idleStatus == IdleStatus.BOTH_IDLE) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("[sessionIdle] session:[%s] close. enter Idle status.", session));
			}
			adminSession.closeIoSession(session, false);
		}
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
		LOGGER.error(String.format("[exceptionCaught] session:[%s]", session), throwable);
	}
	
}
