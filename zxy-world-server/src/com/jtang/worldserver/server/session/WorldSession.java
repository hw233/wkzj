package com.jtang.worldserver.server.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.protocol.ActorResponse;

/**
 * 世界服的Sesion管理
 * @author 0x737263
 *
 */
@Component
public class WorldSession {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldSession.class);
	
	/**
	 * 客户端连接session管理.key:serverId,value:List<IoSession>
	 */
	private static ConcurrentHashMap<Integer, IoSession> CLIENT_SESSION_MAPS = new ConcurrentHashMap<Integer, IoSession>();
	
	private static String SERVER_ID = "serverid";
	
	/**
	 * 收到gameserver的注册包后调用
	 * @param session
	 */
	public void register(Integer serverId, IoSession session) {
		if (session != null) {
			session.setAttribute(SERVER_ID, serverId);
			CLIENT_SESSION_MAPS.put(serverId, session);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("Session [%d]放入到管理后台会话列表, serverId:[%s]", session.getId(), serverId));
			}
		}
	}
	
	/**
	 * 仅限sessionClosed时调用清除session
	 * @param session
	 */
	public void remove(IoSession session) {
		if (session == null) {
			return;
		}

		Integer serverId = (Integer) session.getAttribute(SERVER_ID);
		if (serverId == null) {
			return;
		}

		CLIENT_SESSION_MAPS.remove(serverId);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("remove serverId:[%d] in world session list", serverId));
		}
	}
	
	public void closeIoSession(IoSession session, boolean immediately) {
		if (session != null) {
			session.close(immediately);
		}
	}
	
	/**
	 * 获取serverId
	 * @param session 当前客户端session对象
	 * @return
	 */
	public Integer getServerId(IoSession session) {
		for (Map.Entry<Integer, IoSession> entry : CLIENT_SESSION_MAPS.entrySet()) {
			if (entry.getValue() ==  session) {
				return entry.getKey();
			}
		}
		return 0;
	}
	
	/**
	 * 断开所有session
	 */
	public void closeAllSession() {
		for (IoSession session : CLIENT_SESSION_MAPS.values()) {
			closeIoSession(session, false);
		}
	}
	
	public void broadcast(ActorResponse response) {
		for (IoSession session : CLIENT_SESSION_MAPS.values()) {
			session.write(response);
		}
	}
	
	public void writeRespnse(int serverId, ActorResponse response) {
		IoSession session = CLIENT_SESSION_MAPS.get(serverId);
		if (session != null) {
			session.write(response);
		} else {
			LOGGER.error(String.format("session not exsist, serverId:[%s]",  serverId));
		}
	}
}
