package com.jtang.gameserver.server.session;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 管理员Session处理类
 * @author 0x737263
 *
 */
@Component
public class AdminSession {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminSession.class);

	private static final ConcurrentHashMap<Long, IoSession> ADMIN_SESSION_MAP = new ConcurrentHashMap<Long, IoSession>();

	public void put(IoSession session) {
		if (session != null) {
			ADMIN_SESSION_MAP.put(session.getId(), session);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(String.format("Session [%d]放入到管理后台会话列表", session.getId()));
		}
	}
	
	public void remove(IoSession session) {
		ADMIN_SESSION_MAP.remove(session.getId());
	}
	
	public void closeIoSession(IoSession session, boolean immediately) {
		if (session != null) {
			if (ADMIN_SESSION_MAP.containsKey(session.getId())) {
				ADMIN_SESSION_MAP.remove(session.getId());
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(String.format("Session [%d]从管理后台会话列表删除", session.getId()));
				}
			}
			session.close(immediately);
		}
	}

}
