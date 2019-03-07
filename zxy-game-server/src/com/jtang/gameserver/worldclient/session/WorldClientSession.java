package com.jtang.gameserver.worldclient.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.protocol.ActorRequest;
import com.jtang.core.protocol.Response;
import com.jtang.gameserver.module.crossbattle.model.Game2WorldForward;

/**
 * 世界服Session处理类
 * @author 0x737263
 *
 */
@Component
public class WorldClientSession {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldClientSession.class);

	private List<IoSession> clientSessionList = new ArrayList<>();

	/**
	 * 备份未发送的消息队列
	 */
	private Queue<ActorRequest> backupRequest = new ConcurrentLinkedQueue<>();
	
	public void put(IoSession session) {
		synchronized (clientSessionList) {
			clientSessionList.clear();
			if (session != null) {
				clientSessionList.add(session);
				if (LOGGER.isDebugEnabled())
					LOGGER.debug(String.format("Session [%d]放入到世界服客户端会话列表", session.getId()));
			}
			while (backupRequest.size() > 0) {
				ActorRequest request = backupRequest.poll();
				if (request != null) {
					sendMsg(request);
				}
			}
		}
	}
	
	public void remove(IoSession session) {
		clientSessionList.remove(session);
	}
	
	public void closeIoSession(IoSession session, boolean immediately) {
		if (session != null) {
			session.close(immediately);
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("Session [%d]从世界服客户端会话列表删除", session.getId()));
			}
		}
	}
	
	/**
	 * gamserver收到消息后转发到worldserver
	 * @param response
	 * @param actorId
	 * @param dataArray
	 */
	public boolean game2WorldForward(Response response, long actorId, Object... dataArray) {
		Game2WorldForward fowardData = new Game2WorldForward(dataArray);
		ActorRequest request = ActorRequest.valueOf(response.getModule(), response.getCmd(), actorId, fowardData.getBytes());
		return sendMsg(request);
	}
	
	/**
	 * 发送消息
	 * @param request
	 */
	public boolean sendMsg(ActorRequest request) {
		IoSession client = getClient();
		if (client != null) {
			client.write(request);
			return true;
		} else {
//			backupRequest.add(request);
//			LOGGER.warn("世界服连接不存在");
			return false;
		}
	}
	
	/**
	 * 获取session
	 * @return
	 */
	public IoSession getClient(){
		if (clientSessionList.size() > 0) {
			return clientSessionList.get(0);
		}
		return null;
	}
	
	public List<IoSession> getClients() {
		return clientSessionList;
	}
}
