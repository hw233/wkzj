package com.jtang.gameserver.server.session;

/**
 * Session自定义属性
 * @author 0x737263
 *
 */
public class SessionKey {

	/**
	 * 角色id
	 */
	public static final String ACTOR_ID = "actor_id";
	
	/**
	 * 远程ip
	 */
	public static final String REMOTE_HOST = "remote_host";
	
	/**
	 * 创建session分配的自增唯一id
	 */
	public static final String ATOMIC_ID = "atomic_id";
	
	/**
	 * 玩家登入时当前服务器在线玩家人数
	 */
	public static final String WHICH_CLIENTS = "whichClients";
	
	/**
	 * 第一次请求标识
	 */
	public static final String FIRST_REQUEST = "first_request";
	
	/**
	 * 洪水记录对象
	 */
	public static final String FLOOD_RECORD = "flood_record"; 
	
	/**
	 * 用户登陆的平台类型
	 */
	public static final String PLATFORM_TYPE = "platform_type";
	
	/**
	 * 用户登陆的平台uid
	 */
	public static final String PLATFORM_UID = "platform_uid";
	
	/**
	 * 角色登陆的serverid
	 */
	public static final String SERVER_ID = "server_id";	
}
