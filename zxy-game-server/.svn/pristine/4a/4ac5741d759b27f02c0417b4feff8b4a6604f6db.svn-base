package com.jtang.gameserver.admin;

import com.jtang.core.protocol.StatusCode;

/**
 * 返回状态码(id全局唯一,模块id + 0-99)
 * <pre>
 * 使用方法：
 * 导入 import static com.jtang.sm2.module.StatusCodeConstant.*;
 * </pre>
 * @author 0x737263
 *
 */
public interface GameAdminStatusCodeConstant extends StatusCode {	
	
	/** 开关服务器失败 */
	short MAINTAIN_OPEN_CLOSE_SERVER_ERROR = 101;
	/** 发送公告失败 */
	short MAINTAIN_SEND_NOTICE_ERROR = 102;
	/** 开关服务器参数错误 */
	short MAINTAIN_OPEN_CLOSE_SERVER_ARG_ERROR = 103;
	/** 发送公告参数错误*/
	short MAINTAIN_SEND_NOTICE_ARG_ERROR = 104;
	/** 踢人角色不在线*/
	short MAINTAIN_KICK_ACTOR_OFFLINE = 105;
	/** 配置文件名不能为空  */
	short DATA_CONFIG_NAME_NOT_NULL = 106;
	/** 配置文件内容不能为空 */
	short DATA_CONFIG_NOT_NULL = 107;
	/** 配置文件刷新失败 */
	short DATA_CONFIG_FLUSH_ERROR = 108;
	/** 不支持的表名*/
	short NOT_SUPPORT_TABLE = 109;
	
	
	/** 角色不存在*/
	short ACTOR_NOT_EXIST = 201;
	/** 点券添加参数错误 */
	short TICKET_ADD_ERROR = 202;
	/** 数据库错误*/
	short TICKET_ADD_DB_ERROR = 203;
	/** 订单重复 */
	short TICKET_ADD_ORDER_DUPLICATE_ERROR = 204;
	/** 赠送vip等级失败*/
	short GIVE_VIPLEVEL_ERROR = 205;
	/** 加声望数据错误*/
	short GIVE_REPUTATION_ERROR = 206;
	/** 删除通知ID错误 **/
	short NOTIFY_ID_ERROR = 207;
	
//	/** 加金币参数错误*/
//	short ADD_GOLD_ARG_ERROR = 205;
	
	
	
	
	/** 加金币参数错误*/
	short ADD_GOLD_ARG_ERROR = 302;
	/** 数据库错误 */
	short GOLD_ADD_DB_ERROR = 303;
	/** 添加装备错误 */
	short ADD_EQUIP_ERROR = 304;
	/** 扣除金币错误*/
	short DECREASE_GOLD_ERROR = 305;
	/** 金币不足 */
	short GOLD_NOT_ENOUGH = 306;
	/**删除活动失败*/
	short DELETE_ACTIVE_FILE = 307;
	
	
	/** 添加物品错误 */
	short ADD_GOODS_ERROR = 501;
	/**物品配置id错误*/
	short GOODS_CONFIG_NOT_EXISTS = 502;
	
	
	
	/**装备不存在*/
	short EQUIP_NOT_EXIST = 601;
	
	
	/**盟友不存在*/
	short ALLY_NOT_EXIST = 701;
	
	/**还没有打过任何战场*/
	short BATTLE_IS_FIRST = 901;
	/**战场已经打完*/
	short BATTLE_IS_LAST = 902;
	
	
	/**强化室修改等级错误*/
	short ENHANCED_UP_ERROR = 1401;
	/**超过强化室等级上限*/
	short ENHANCED_UP_LEVEL_MAX = 1402;
	
	/**目标角色不存在 */
	short TARGET_ACTOR_NOT_EXIST = 1501;
	
	/** 聊天禁言  用户没有被禁言，无法解禁*/
	short CHAT_NOT_FORBIDDEN = 1801;
	/**聊天禁言    用户已经被禁言*/
	short CHAT_ALREADY_BE_FORBIDDEN = 1802;
	/** 聊天禁言  禁言结束时间错误*/
	short CHAT_FORBIDDEN_TIME_ERROR = 1803;
	/** 聊天禁言  禁言角色不存在*/
	short CHAT_FORBIDDEN_ACTOR_NOT_EXIST = 1804;
	
}
