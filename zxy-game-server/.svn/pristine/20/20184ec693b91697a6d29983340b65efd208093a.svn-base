package com.jtang.gameserver.server.filter;

import static com.jtang.core.protocol.StatusCode.FIREWALL_BLOCK;
import static com.jtang.core.protocol.StatusCode.MAX_CLIENT_LIMIE;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.gameserver.server.firewall.Firewall;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 防火墙过滤器类
 * warnning!!!该filter必需放到集合最前面.
 * @author 0x737263
 *
 */
@Component
public class FirewallFilter extends IoFilterAdapter {

	private static final Log LOGGER = LogFactory.getLog(FirewallFilter.class);

	@Autowired
	private Firewall firewall;

	@Autowired
	private PlayerSession playerSession;

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		
		//如果开始防火墙，则进行过滤
		if (firewall.isEnableFirewall()) {
			
			//如果sesion没有分配唯一id,或者已经超过限制。则关闭连接
			Integer currClients = playerSession.getAtomicId(session);
			if (currClients == null || firewall.isMaxClientLimit(currClients)) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(String.format("firewall is maxConnection. curClients:[%s] sessionId:[%s]", currClients, session.getId()));
				}
				playerSession.closeIoSession(session, true);
				return;
			}
			
			boolean blocked = firewall.isBlocked(session);
			// 包大小监控
			if (blocked == false && (message instanceof IoBuffer)) {
				IoBuffer ioBuffer = (IoBuffer) message;
				int length = ioBuffer.remaining();
				blocked = firewall.blockedByBytes(session, length);
			}

			// 包数量监控
			if (blocked == false) {
				blocked = this.firewall.blockedByPacks(session, 1);
			}

			// 封号
			if (blocked) {
				String remoteIp = playerSession.getRemoteIp(session);
				long actorId = playerSession.getActorId(session);
				LOGGER.warn(String.format("In blacklist: [ip: %s, actorId: %d] sessionId:[%s]", remoteIp, actorId, session.getId()));
				playerSession.closeIoSession(session, true);
				return;
			}
		}

		super.messageReceived(nextFilter, session, message);
	}

	@Override
	public void sessionCreated(NextFilter nextFilter, IoSession session) throws Exception {
		// 创建Session时，分配一个唯一自增id
		int currClients = this.firewall.increaseClients();

		if ((this.firewall.isBlocked(session))) {
			Long actorId = playerSession.getActorId(session);
			LOGGER.info(String.format("firewall is blocked actorId:[%s] sessionId:[%s]", actorId, session.getId()));
			playerSession.writeStatusCode(session, FIREWALL_BLOCK, false);
			return;
		}

		if (this.firewall.isMaxClientLimit(currClients)) {
			playerSession.writeStatusCode(session, MAX_CLIENT_LIMIE, false);
			LOGGER.info("Connections limit, close session...");
			return;
		}

		playerSession.setAtomicId(session, currClients);
		super.sessionCreated(nextFilter, session);
	}

	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception {
		super.sessionOpened(nextFilter, session);
	}

	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
		this.firewall.decreaseClients();
		super.sessionClosed(nextFilter, session);
	}
}
