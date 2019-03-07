package com.jiatang.common.baseworld;

/**
 * game-server与world-server基础通讯命令
 * @author 0x737263
 *
 */
public interface BaseWorldCmd {

	/**
	 * 心跳包
	 * <pre>
	 * 请求: {@code }
	 * 响应: {@code }
	 * </pre>
	 */
	byte HEART_BEAT = 0;
	
	/**
	 * session注册
	 * <pre>
	 * 请求:{@code SessionRegisterRequest}
	 * 响应:{@code Response}
	 * </pre>
	 */
	byte REGISTER = 1;
	
	/**
	 * 世界服关机
	 */
	byte SHTUDOWN = 2;
	
	/**
	 * 热刷配置
	 */
	byte RELOAD_CONFIG = 3;
}
