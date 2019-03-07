package com.jtang.gameserver.server.broadcast;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.protocol.Response;
import com.jtang.core.thread.NamedThreadFactory;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 消息广播类
 * 
 * @author 0x737263
 * 
 */
@Component
public final class Broadcast {

	private static final Logger LOGGER = LoggerFactory.getLogger(Broadcast.class);

	/**
	 * 玩家会话
	 */
	@Autowired
	private PlayerSession playerSession;

	/**
	 * 广播队列配置大小(固定2个)
	 */
	private final int COMMON_QUEUE_SIZE = 2;

	/**
	 * 广播列表集合
	 */
	@SuppressWarnings("unchecked")
	private final BlockingQueue<BroadcastContext>[] PUSHER_QUEUE_ARRAY = new LinkedBlockingQueue[this.COMMON_QUEUE_SIZE];

	/**
	 * init
	 */
	@PostConstruct
	void initialize() {
		String threadName = "boardcast sync thread";

		for (int index = 0; index < this.COMMON_QUEUE_SIZE; index++) {
			BlockingQueue<BroadcastContext> queue = this.PUSHER_QUEUE_ARRAY[index];
			if (queue == null) {
				this.PUSHER_QUEUE_ARRAY[index] = new LinkedBlockingQueue<BroadcastContext>(Integer.MAX_VALUE);
				queue = this.PUSHER_QUEUE_ARRAY[index];
			}

			NamedThreadFactory factory = new NamedThreadFactory(new ThreadGroup(threadName), threadName);
			Thread thread = factory.newThread(createBoardcastThread(queue));
			thread.setDaemon(true);
			thread.start();
		}
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("broadcast sync thread init complete! total thread:[{}]", this.COMMON_QUEUE_SIZE);
		}
	}
	
	/**
	 * 创建推送异步线程
	 * 
	 * @param commonQueue
	 * @return
	 */
	public final Runnable createBoardcastThread(final BlockingQueue<BroadcastContext> commonQueue) {
		return new Runnable() {
			public void run() {
				try {
					while (true) {
						BroadcastContext pushContext = commonQueue.take();
						Response response = pushContext.getResponse();

						byte module = response.getModule();
						byte cmd = response.getCmd();
						BroadcastType type = pushContext.getType();
						Collection<Long> actorIdList = pushContext.getActorIdList();

						if (type == BroadcastType.ALL) {
							//TODO 这里有点不太好。万一谁设置错为了ALL.那不是所有人都收到广播了。
							actorIdList = playerSession.onlineActorList();
						}
						playerSession.push(actorIdList, response);

						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(String.format("push message successed.[module: %d, cmd: %d ]", module, cmd));
						}
					}
				} catch (Exception ex) {
					LOGGER.error("Error: {}", ex);
				}
			}
		};
	}
	
	
	/**
	 * 推送消息
	 * 
	 * @param actorId 接收消息玩家id
	 * @param response 返回消息结构
	 *            
	 */
	public void push(long actorId, Response response) {
		put2Queue(BroadcastContext.push2Actor(actorId, response));
	}

	/**
	 * 推送消息
	 * 
	 * @param actorIdList 接收消息玩家列表id
	 * @param response 返回消息结构
	 *            
	 */
	public void push(Collection<Long> actorIdList, Response response) {
		if (actorIdList != null && !actorIdList.isEmpty()) {
			put2Queue(BroadcastContext.push2Actors(actorIdList, response));
		}
	}

	/**
	 * 推送消息给所有在线已验证用户
	 * 
	 * @param response 返回消息结构
	 * 
	 */
	public void push2AllOnline(Response response) {
		put2Queue(BroadcastContext.push2AllOnline(response));
	}

	/**
	 * 推送异步队列
	 * 
	 * @param boardContext 广播上下文
	 *            
	 */
	private void put2Queue(BroadcastContext boardContext) {
		if (boardContext == null || boardContext.getResponse() == null) {
			return;
		}

		long currentTimeMillis = System.currentTimeMillis();
		int index = (int) (currentTimeMillis % this.COMMON_QUEUE_SIZE);
		try {
			this.PUSHER_QUEUE_ARRAY[index].add(boardContext);
			
			if (LOGGER.isDebugEnabled()) {
				Response response = boardContext.getResponse();
				LOGGER.debug(String.format("boardcast into the queue. module: [%d], cmd: [%d]", response.getModule(), response.getCmd()));
			}
		} catch (Exception ex) {
			LOGGER.error("put2Queue error: {}", ex);
		}
	}

}