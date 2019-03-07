package com.jtang.gameserver.worldclient.baseworld;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jiatang.common.baseworld.BaseWorldCmd;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.ActorRequest;
import com.jtang.core.protocol.ActorResponse;
import com.jtang.core.thread.NamedThreadFactory;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.worldclient.router.WorldClientRouterHandlerImpl;
import com.jtang.gameserver.worldclient.session.WorldClientSession;

@Component
public class BaseWorldCallbackHandler extends WorldClientRouterHandlerImpl {
	private static final Log LOGGER = LogFactory.getLog(BaseWorldCallbackHandler.class);

	private final ReentrantLock takeLock = new ReentrantLock();	
	private final Condition condition = this.takeLock.newCondition();
	
	@Autowired
	private WorldClientSession worldClientSession;

	
	/**
	 * 心跳间隔（毫秒）
	 */
	private int heartBeatTime = 5000;
	
	@Override
	public byte getModule() {
		return ModuleName.BASE;
	}
	
	@PostConstruct
	public void init() {
		heartBeatThread();
	}
	
	@Cmd(Id = BaseWorldCmd.HEART_BEAT)
	public void heartBeat(IoSession session, ActorResponse response) {

	}
	
	@Cmd(Id = BaseWorldCmd.REGISTER)
	public void register(IoSession session, ActorResponse response) {
		LOGGER.info("注册成功");
	}
	
	private void heartBeatThread() {
		ThreadGroup threadGroup = new ThreadGroup("世界服客户端");
		NamedThreadFactory threadFactory = new NamedThreadFactory(threadGroup, "世界服客户端心跳线程");

		final ActorRequest request = (ActorRequest) ActorRequest.valueOf(ModuleName.BASE, BaseWorldCmd.HEART_BEAT);
		
		Thread thread = threadFactory.newThread(new Runnable() {
			@Override
			public void run() {
				if (Game.worldServerEnable() == false) {
					return;
				}
				while (true) {
					worldClientSession.sendMsg(request);
					
					try {
						takeLock.lockInterruptibly();
						try {
							condition.await(heartBeatTime, TimeUnit.MILLISECONDS);
						} catch (Exception e) {
							LOGGER.error("{}", e);
						} finally {
							takeLock.unlock();
						}

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
