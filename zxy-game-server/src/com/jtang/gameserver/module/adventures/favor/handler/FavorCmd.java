package com.jtang.gameserver.module.adventures.favor.handler;


/**
 * 福神眷顾命令接口
 * @author 0x737263
 *
 */
public interface FavorCmd {
	
	/**
	 * 获取奇遇-福神眷顾的信息
	 * <pre>
	 * 请求:{@code Request}
	 * 响应:{@code FavorResponse}
	 * </pre>
	 */
	byte GET_INFO = 1;
	
	/**
	 * 使用特权
	 * <pre>
	 * 请求:{@code PrivilegeUseRequest}
	 * 响应:{@code PrivilegeUseResponse}
	 * </pre>
	 */
	byte USE_PRIVILEGE = 2;
	
	/**
	 * 激活
	 * <pre>
	 * 推送:{@code FavorResponse}
	 * </pre>
	 */
	byte PUSH_FAVOR_ACTIVE = 3;
	
	/**
	 * 消失
	 * <pre>
	 * 推送{@code Response}
	 * </pre>
	 */
	byte CHANGE_FAVOR_DISAPPER= 4;
	/**
	 * 重置
	 * 推送{@code FavorResponse}
	 */
	byte PUSH_RESET = 5;
	
	
}
