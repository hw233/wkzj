package com.jtang.gameserver.server;

import org.apache.mina.core.service.IoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.context.SpringContext;
import com.jtang.core.mina.SocketServer;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.server.codec.GatewayAdminServerDecoder;
import com.jtang.gameserver.server.codec.GatewayAdminServerEncoder;
import com.jtang.gameserver.server.iohandler.AdminServerIoHandler;

/**
 * 游戏管理接口服务器socket服务器启动类
 * @author 0x737263
 *
 */
public class AdminSocketServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminSocketServer.class);
	
	private SocketServer server;
	
	public AdminSocketServer() {
		IoHandler ioHandler = (IoHandler) SpringContext.getBean(AdminServerIoHandler.class);
		
		server = new SocketServer("gateway admin server", new GatewayAdminServerEncoder(), new GatewayAdminServerDecoder(), ioHandler);
		server.setReadBufferSize(1024 * 1024 * 2); // 2mb
		server.setReceiveBufferSize(1024 * 1024 * 2);
	}
	
	/**
	 * 启动
	 * @throws Exception
	 */
	public void start() {
		try {
			server.start(Game.getAdminPort());
		} catch (Exception ex) {
			LOGGER.error("admin server", ex);
			System.exit(0);
		}
	}

}
