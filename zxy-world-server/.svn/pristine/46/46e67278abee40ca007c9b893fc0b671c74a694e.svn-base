package com.jtang.worldserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.worldserver.server.WorldSocketServer;

/**
 * 世界服启动器
 * @author 0x737263
 *
 */
public class WorldLauncher {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldLauncher.class);

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {		
		LOGGER.info("========== world server launcher  ==========");
		
		new WorldSocketServer().start();
	}

}
