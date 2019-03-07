package com.jtang.worldserver.server;

import org.apache.mina.core.service.IoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.context.SpringContext;
import com.jtang.core.mina.SocketServer;
import com.jtang.worldserver.component.World;
import com.jtang.worldserver.component.oss.WorldOssLogger;
import com.jtang.worldserver.server.codec.WorldServerDecoder;
import com.jtang.worldserver.server.codec.WorldServerEncoder;
import com.jtang.worldserver.server.iohandler.WorldServerIoHandler;

public class WorldSocketServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldSocketServer.class);
	
	private SocketServer server;

	public WorldSocketServer() {
		IoHandler ioHandler = (IoHandler) SpringContext.getBean(WorldServerIoHandler.class);

		server = new SocketServer("world server", new WorldServerEncoder(), new WorldServerDecoder(), ioHandler);
		server.setReadBufferSize(1024 * 1024 * 2); // 2mb
		server.setReceiveBufferSize(1024 * 1024 * 2);
	}
	
	/**
	 * 启动
	 * @throws Exception
	 */
	public void start() {
		try {
			server.start(World.getWorldPort());
		} catch (Exception ex) {
			LOGGER.error("world server", ex);
		}
		
		// add shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				LOGGER.info("stopping world server");
				WorldOssLogger.reflushLogger(); //刷新内存日志到硬盘
				server.stop();
			}
		}));
	}
}
