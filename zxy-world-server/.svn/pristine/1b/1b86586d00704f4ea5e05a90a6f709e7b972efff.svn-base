package com.jtang.worldserver.component;

import java.util.Properties;

import com.jiatang.common.type.WorldState;
import com.jtang.core.result.ObjectReference;
import com.jtang.core.utility.PropertiesUtils;

/**
 * world server相关配置
 * @author 0x737263
 *
 */
public class World {
	private static ObjectReference<World> ref = new ObjectReference<World>();

	public static WorldState state = WorldState.OPEN;
	
	private int worldPort;
	
	private String version;
	
	static {
		Properties p = PropertiesUtils.read("world.properties");
		World world = new World();
		world.worldPort = PropertiesUtils.getInt(p, "world_port");
		world.version = PropertiesUtils.getString(p, "version");
		
		ref.set(world);
	}
	
	/**
	 * 世界服监听端口
	 * @return
	 */
	public static int getWorldPort() {
		return ref.get().worldPort;
	}
	
	/**
	 * 世界服版本
	 * @return
	 */
	public static String getVersion() {
		return ref.get().version;
	}
	
}
