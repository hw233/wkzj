package com.jtang.gameserver.component;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.jtang.core.result.ObjectReference;
import com.jtang.core.utility.PropertiesUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.admin.type.MaintainState;

/**
 * 游戏全局配置属性 game.properties
 * @author 0x737263
 *
 */
public class Game {
	private static ObjectReference<Game> ref = new ObjectReference<Game>();

	public static volatile MaintainState maintain = MaintainState.OPEN;
	private static boolean debugmode = false;

	private int serverId = 1;
	private List<Integer> platformList = new ArrayList<>();
	private String version;
	private int gatewayPort = 9123;
	private int adminPort = 9124;
	private List<String> maintainIpList = new ArrayList<>();
	private List<String> adminIpList = new ArrayList<>();
	
	private boolean worldServerEnable = true;
	private int worldServerReconnectTime = 1000;
	private String worldServerIp;
	private int worldServerPort;
	
	private List<String> debug_uids = new ArrayList<>();
	
	private boolean skill_debug = false;
	
	/**
	 * 服务器进程id
	 */
	public static long PID;
	
	private Game() {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		String name = runtimeMXBean.getName();
		PID = Long.valueOf(StringUtils.split(name, "@")[0]);
	}
	
	static {
		Properties p = PropertiesUtils.read("game.properties");

		maintain = MaintainState.getByType(PropertiesUtils.getInt(p, "maintain"));
		debugmode = Boolean.valueOf(PropertiesUtils.getString(p, "debug_mode"));
		
		Game game = new Game();
		game.serverId = PropertiesUtils.getInt(p, "server_id");
		game.platformList = PropertiesUtils.dotSplitIntList(p, "platform_id");
		
		game.gatewayPort = PropertiesUtils.getInt(p, "gateway_port");
		game.adminPort = PropertiesUtils.getInt(p, "admin_port");
		game.maintainIpList = PropertiesUtils.dotSplitStringList(p, "maintain_ip");
		game.adminIpList = PropertiesUtils.dotSplitStringList(p, "admin_ip");
		game.debug_uids = PropertiesUtils.dotSplitStringList(p, "debug_uid");
		game.worldServerEnable = Boolean.valueOf(PropertiesUtils.getString(p, "world_server_enable"));
		game.worldServerReconnectTime = PropertiesUtils.getInt(p, "world_server_reconnect_time");
		game.worldServerIp = PropertiesUtils.getString(p, "world_server_ip");
		game.worldServerPort = PropertiesUtils.getInt(p, "world_server_port");
		game.skill_debug = Boolean.valueOf(PropertiesUtils.getString(p, "skill_debug"));
		
		Properties versionProperties = PropertiesUtils.read("version.properties");
		game.version = PropertiesUtils.getString(versionProperties, "version");

		ref.set(game);
	}

	/**
	 * 是否处于debug模式
	 * @return
	 */
	public static boolean isDebug() {
		return debugmode;
	}
	
	/**
	 * 获取游戏服id
	 * @return
	 */
	public static int getServerId() {
		return ref.get().serverId;
	}
	
	public static void setServerId(int serverId) {
		ref.get().serverId = serverId;
	}
	
	public static String getPlatformString() {
		List<Integer> list = ref.get().platformList;
		if (list.isEmpty()) {
			return "all";
		}
		return StringUtils.collection2SplitString(list, Splitable.BETWEEN_ITEMS);
	}
	
	public static int getAdminPort() {
		return ref.get().adminPort;
	}
	
	public static int getGatewayPort() {
		return ref.get().gatewayPort;
	}
	
	public static void setGatewayPort(int port) {
		ref.get().gatewayPort = port;
	}
	
	/**
	 * 是否为允许的平台id(如果平台id配置为空，则允许所有平台)
	 * @param platformId	平台id
	 * @return
	 */
	public static boolean allowPlatform(int platformId) {
		List<Integer> list = ref.get().platformList;
		if (list.isEmpty()) {
			return true;
		}

		boolean result = false;
		for (int i : list) {
			if (i == platformId) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**
	 * 版本号
	 */
	public static String getVersion() {
		return ref.get().version;
	}
	
	/**
	 * 检查是否是允许访问的ip
	 * @param ip
	 * @return
	 */
	public static boolean checkAllowIP(String ip) {
		if (maintain.equals(MaintainState.OPEN)) {
			return true;
		} else if (maintain.equals(MaintainState.MAINTAIN)) {
			if (ref.get().maintainIpList.contains(ip)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	/**
	 * 检查是否是允许的ip
	 * @param uid
	 * @return
	 */
	public static boolean checkAllowUID(String uid) {
		if (ref.get().debug_uids.isEmpty() == false) {
			if (ref.get().debug_uids.contains(uid)) {
				return true;
			} else {
				return false;
			}
		} 
		return true;
	}
	
	/**
	 * 检查IP是否可访问（如果不配置，任意可访问，配置了就是指定的访问）
	 * @param ip
	 * @return
	 */
	public static boolean checkAdminIP(String ip) {
		if (ref.get().adminIpList.size() == 0) {
			return true;
		} else {
			return ref.get().adminIpList.contains(ip);
		}
	}

	
	public static List<String> getAdminIpList(){
		return ref.get().adminIpList;
	}
	public static List<String> getMaintainIpList(){
		return ref.get().maintainIpList;
	}

	public static boolean worldServerEnable() {
		return ref.get().worldServerEnable;
	}
	
	public static int worldServerReconnectTime() {
		return ref.get().worldServerReconnectTime;
	}
	
	public static String worldServerIp() {
		return ref.get().worldServerIp;
	}
	
	public static int worldServerPort() {
		return ref.get().worldServerPort;
	}
	
	public static List<String> getDebugUids(){
		return ref.get().debug_uids;
	}
	
	public static void addDebugUids(String uid){
		ref.get().debug_uids.add(uid);
	}

	public static boolean skillDebug(){
		return ref.get().skill_debug;
	}
}
