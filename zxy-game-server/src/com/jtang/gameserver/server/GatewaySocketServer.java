package com.jtang.gameserver.server;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.context.SpringContext;
import com.jtang.core.mina.SocketServer;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.server.codec.GatewayServerDecoder;
import com.jtang.gameserver.server.codec.GatewayServerEncoder;
import com.jtang.gameserver.server.filter.FirewallFilter;
import com.jtang.gameserver.server.iohandler.GateServerIoHandler;

/**
 * 网关服务器socket服务器启动类
 * @author 0x737263
 *
 */
public class GatewaySocketServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewaySocketServer.class);
	public static Boolean tcpNoDelay = (Boolean)SpringContext.getBean("gatewayServer.mina.tcp.nodelay");
	public static Integer backLog = (Integer)SpringContext.getBean("gatewayServer.mina.backlog");
	public static Integer readBufferSize = (Integer)SpringContext.getBean("gatewayServer.mina.read.buffer.size");
	public static Integer sendBufferSize = (Integer)SpringContext.getBean("gatewayServer.mina.send.buffer.size");
	public static Integer receiveBufferSize = (Integer)SpringContext.getBean("gatewayServer.mina.receive.buffer.size");
	public static Integer bothIdleTime = (Integer)SpringContext.getBean("gatewayServer.mina.both.idle.time");
	public static Integer writeTimeout = (Integer)SpringContext.getBean("gatewayServer.mina.write.timeout");
	public static Integer workPoolMin = (Integer)SpringContext.getBean("gatewayServer.mina.work.pool.min");
	public static Integer workPoolMax = (Integer)SpringContext.getBean("gatewayServer.mina.work.pool.max");
	public static Integer workPoolIdle = (Integer)SpringContext.getBean("gatewayServer.mina.work.pool.idle");
	
	private SocketServer server;
	private static GatewaySocketServer instance;

	private GatewaySocketServer() {
		IoHandler ioHandler = (IoHandler) SpringContext.getBean(GateServerIoHandler.class);
		IoFilter firewallFilter = (IoFilter) SpringContext.getBean(FirewallFilter.class);

		server = new SocketServer("gateway server", new GatewayServerEncoder(), new GatewayServerDecoder(), ioHandler, firewallFilter);
		server.setWorkPool(workPoolMin, workPoolMax, workPoolIdle);
		server.setBackLog(backLog);
		server.setTcpNoDelay(tcpNoDelay);
		server.setReadBufferSize(readBufferSize);
		server.setSendBufferSize(sendBufferSize);
		server.setReceiveBufferSize(receiveBufferSize);
		server.setWriteTimeout(writeTimeout);
		server.setBothIdleTime(bothIdleTime);
	}
	
	public static GatewaySocketServer getInstance(){
		if (instance == null) {
			instance = new GatewaySocketServer();
		}
		return instance;
	}
	
	/**
	 * 启动
	 */
	public void start() {
		try {
			server.start(Game.getGatewayPort());
			LOGGER.info(String.format("gateway start complete! serverid:[%s] platformid:[%s]", Game.getServerId(), Game.getPlatformString()));
		} catch (Exception ex) {
			LOGGER.error("gateway server", ex);
			System.exit(0);
		}
		
		// add shutdown hook
//		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//			public void run() {
//				LOGGER.info("stopping gateway server");
//				GameOssLogger.reflushLogger(); //刷新内存日志到硬盘
//				server.stop();
//			}
//		}));
	}

	public void stop() {
		LOGGER.info("stopping gateway server");
		if (server != null) {
			server.stop();
		}
		GameOssLogger.reflushLogger(); //刷新内存日志到硬盘
	}

}
