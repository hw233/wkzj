package com.jtang.gameserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.context.SpringContext;
import com.jtang.core.utility.UUIDUtils;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.server.AdminSocketServer;
import com.jtang.gameserver.server.GatewaySocketServer;
import com.jtang.gameserver.worldclient.WorldSocketClient;

/**
 * 启动器
 * @author 0x737263
 *
 */
public class GameLauncher {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameLauncher.class);
	
	public static void main(String[] args) throws Exception {		
		LOGGER.info("========== game server launcher  ==========");		
		checkGameConfig(args);
	}
	
	private static void checkGameConfig(String[] args) {
		// debug config -> program arguments
		if (args != null) {			
			if(args.length > 0) {
				Game.setServerId(Integer.valueOf(args[0]));	
			}
			
			if(args.length > 1) {
				Game.setGatewayPort(Integer.valueOf(args[1]));
			}
		}
		
		if (Game.getServerId() < 1 || Game.getServerId() > 0xfff) {
			throw new RuntimeException("serverid value config is error in game.properties");
		}
		
		if(Game.getGatewayPort() < 1) {
			throw new RuntimeException("gateway port value config is error in game.properties");			
		}
		// 设置启动时uuid基数
		IdTableJdbc jdbc = (IdTableJdbc) SpringContext.getBean(IdTableJdbc.class);
		int temp = jdbc.queryForInt("SELECT UNIX_TIMESTAMP()");
		UUIDUtils.setBase(temp);
		//游戏管理接口启动
		new AdminSocketServer().start();
		//世界服客户端启动
		new WorldSocketClient().start();
		//网关启动
		GatewaySocketServer.getInstance().start();
		
	}
}
