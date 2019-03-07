package com.jtang.gameserver.admin.handler;





public interface MaintainCmd {

	/**
	 * 改变服务器状态
	 * <pre>
	 * 请求: {@code OpenCloseServerRequest}
	 * 响应: {@code Response}
	 * </pre>
	 */
	byte CHANGE_SERVER_STATE = 0;
	
	/**
	 * 发送系统公告
	 * 请求: {@code SystemNoticeRequest}
	 * 响应: {@code Response}
	 */
	byte SEND_SYSTEM_NOTICE = 1;
	
	/**
	 * 获取在线人数
	 * 请求:{@code Request}
	 * 响应:{@code OnlinePlayerNumResponse}
	 */
	byte ONLINE_NUM = 3;
	
	/**
	 * 关闭服务器
	 * 请求:{@code Request}
	 * 响应: {@code Response}
	 */
	byte SHUTDOWN_SERVER = 4;
	
	/**
	 * 心跳消息
	 */
	byte HEARTBEAT = 5;

	/**
	 * 踢出玩家
	 * 请求: {@code KickPlayerRequest}
	 * 响应: {@code Response}
	 */
	byte KICK_OF_PLAYER = 6;
	
	/**
	 * 获取服务器状态
	 * 请求:{@code Request}
	 * 响应:{@code ServerStateResponse}
	 */
	byte GET_SERVER_STATE = 7;
	
	/**
	 * 刷新配置文件
	 * 请求:{@code FlushDataConfigRequest}
	 * 响应:{@code Response}
	 */
	byte FLUSH_DATA_CONFIG = 8;
	
	/**
	 * 手动提交所有db实体
	 * 请求:{@code Request}
	 * 响应:{@code Response}
	 */
	byte SUBMIT_DB_ENTITY = 9;
	/**
	 * 查看DB实体缓存信息
	 */
	byte DB_ENTITY_INFO = 10;
	
	/**
	 * 查询角色实体
	 */
	byte GET_ACTOR_ENTITY = 11;
	
	/**
	 * 增加uid
	 */
	byte ADD_UID = 12;
	
	/**
	 * 清空UID列表
	 */
	byte CLEAN_UID = 13;
	
	/**
	 * 清理运行时服务器数据库缓存
	 */
	byte CLEAR_DB_INFO = 14;
}
