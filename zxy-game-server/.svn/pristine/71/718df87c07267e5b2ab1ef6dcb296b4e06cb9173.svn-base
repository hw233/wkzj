package com.jtang.gameserver.worldclient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandler;

import com.jtang.core.context.SpringContext;
import com.jtang.core.mina.SocketClient;
import com.jtang.core.thread.NamedThreadFactory;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.worldclient.codec.WorldClientDecoder;
import com.jtang.gameserver.worldclient.codec.WorldClientEncoder;
import com.jtang.gameserver.worldclient.iohandler.WorldClientIoHandler;

/**
 * 世界服客户端
 * @author ludd
 *
 */
public class WorldSocketClient {
	private static final Log LOGGER = LogFactory.getLog(WorldSocketClient.class);
	public static Boolean tcpNoDelay = (Boolean) SpringContext.getBean("worldClient.mina.tcp.nodelay");
	public static Integer backLog = (Integer) SpringContext.getBean("worldClient.mina.backlog");
	public static Integer sendBufferSize = (Integer)SpringContext.getBean("worldClient.mina.send.buffer.size");
	public static Integer receiveBufferSize = (Integer)SpringContext.getBean("worldClient.mina.receive.buffer.size");
	public static Integer connectCheckTime = (Integer)SpringContext.getBean("worldClient.connectCheck.time");
	
	/**
	 * 互斥同步锁
	 */
	private final ReentrantLock takeLock = new ReentrantLock();
	private final Condition condition = this.takeLock.newCondition();
	
	/**
	 * 启动
	 */
	public void start() {
		if (Game.worldServerEnable() == false) {
			LOGGER.info("connect world server setting is disabled.");
			return;
		}

		String clientName = "world server";
		IoHandler ioHandler = (IoHandler) SpringContext.getBean(WorldClientIoHandler.class);
		final SocketClient socket = new SocketClient(clientName, new WorldClientEncoder(), new WorldClientDecoder(), ioHandler);
		socket.start(Game.worldServerIp(), Game.worldServerPort());
		NamedThreadFactory threadFactory = new NamedThreadFactory(new ThreadGroup(clientName), clientName);
        
    	Thread thread = threadFactory.newThread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						takeLock.lockInterruptibly();
						try {
							condition.await(connectCheckTime, TimeUnit.MILLISECONDS); //检测连接等待
						} catch (Exception e) {
							LOGGER.error("{}", e);
						} finally {
							takeLock.unlock();
						}
						socket.reconnect();
						
//						CrossBattleFacade crossBattleFacade = (CrossBattleFacade) SpringContext.getBean(CrossBattleFacadeImpl.class);
//						crossBattleFacade.signup(92274731L);
					} catch (InterruptedException e1) {
						LOGGER.error("{}", e1);
					}
				}
			}
		});
		
		thread.setDaemon(true);
		thread.start();
	}
	

}
