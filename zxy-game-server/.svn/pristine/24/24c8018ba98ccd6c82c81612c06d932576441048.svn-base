package com.jtang.gameserver.module.user.type;

/**
 * 踢下线类型
 * @author 0x737263
 *
 */
public enum KickOffType {

	/**
	 * 账号重复登录, 或者在其他地方上线 
	 */
	LOGIN_DUPLICATE(1),
	
	/**
	 * 服务器即将停服
	 */
	CLOSEING(2),
	
	/**
	 * 玩家被封禁
	 */
	USER_BLOCK(3),
	
	/**
	 * 服务器维护中
	 */
	SERVER_MAINTENANCE(4), 
	/**
	 * 角色重生
	 */
	RESET_ACTOR(5);
	
	private int code;
	
	private KickOffType(int code) {
		this.setCode(code);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
}
