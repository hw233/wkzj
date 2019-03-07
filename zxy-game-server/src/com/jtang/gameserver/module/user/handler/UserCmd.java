package com.jtang.gameserver.module.user.handler;

/**
 * 用户模块命令集合
 * 
 * @author 0x737263
 * 
 */
public interface UserCmd {

	/**
	 * 心跳包
	 * <pre>
	 * 请求: {@code HeartBeatRequest}
	 * 响应: {@code HeartBeatResponse}
	 * </pre>
	 */
	byte HEART_BEAT = 0;
	
	/**
	 * 用户登陆 (第三方验证)
	 * <pre>
	 * 请求: {@code UserLoginRequest}
	 * 响应: {@code UserLoginResponse}
	 * <pre>
	 */
	byte USER_LOGIN = 1;
	
	/**
	 * 获取角色信息
	 * <pre>
	 * 请求：{@code GetActorRequest}
	 * 响应:{@code GetActorResponse}
	 * <pre>
	 */
	byte GET_ACTOR = 2;
	
	/**
	 * 创建角色
	 * <pre>
	 * 根据请求结果决定是否创建角色
	 * 请求：{@code CreateActorRequest}
	 * 响应:{@code CreateActorResponse}
	 * </pre>
	 */
	byte CREATE_ACTOR = 3;
	
	/**
	 * 角色登陆
	 * <pre>
	 * 请求:{@code ActorLoginRequest}
	 * 响应:{@code ActorLoginResponse}
	 * <pre>
	 */
	byte ACTOR_LOGIN = 4;
	
	/**
	 * 推送角色属性
	 * <pre>
	 * 请求：{@code Request}
	 * 响应:{@code ActorAttributeResponse}
	 * 响应:{@code UserDisabledResponse}
	 * </pre>
	 */
	byte PUSH_ACTOR_ATTRIBUTE = 5;

	/**
	 * 设置新手引导流程步骤
	 * <pre>
	 * 请求:{@code SaveGuidesStepRequest}
	 * 响应:{@code Response}
	 * </pre>
	 */
	byte SAVE_GUIDES_STEP = 7;
	
	/**
	 * 保存角色行为动作
	 * <pre>
	 * 请求:{@code ActorMontionRequest}
	 * 响应:无
	 * </pre>
	 */
	byte SAVE_MONTION = 8;
	
	/**
	 * 踢人下线
	 * <pre>
	 * 请求:无
	 * 推送:{@code KickOffResponse}
	 * </pre>
	 */
	byte KICK_OFF = 9;
	
	/**
	 * 用户重连
	 * <pre>
	 * 请求:{@code UserReconnectRequest}
	 * 响应:{@code UserLoginResponse}
	 * </pre>
	 */
	byte USER_RECONNECTION = 10;
	
	/**
	 * 设置新手引导流程步骤
	 * <pre>
	 * 请求:{@code SavePushKeyRequest}
	 * 响应:{@code Response}
	 * </pre>
	 */
	byte SAVE_PUSH_KEY = 11;
	
	/**
	 * 修改玩家名称
	 * 请求:{@code ActorRenameRequest}
	 * 响应:{@code Response}
	 */
	byte ACTOR_RENAME = 12;
	
	/**
	 * 点券补满精力
	 * 请求:{@code Request}
	 * 返回:{@code Response}
	 */
	byte FULL_ENERGY = 13;
	
	/**
	 * 点券补满活力
	 * 请求:{@code Request}
	 * 返回:{@code Response}
	 */
	byte FULL_VIT = 14;
	
	/**
	 * 点券购买金币
	 * 请求:{@code Request}
	 * 返回:{@code Response}
	 */
	byte BUY_GOLD = 15;
	
	/**
	 * 获取主页面购买信息
	 * 请求:{@code Request}
	 * 返回:{@code ActorBuyResponse}
	 * 推送:{@code ActorBuyResponse}
	 */
	byte BUY_INFO = 16;

	/**
	 * 推送阵容首个仙人信息
	 */
	byte PUSH_LINEUP_FIRST_HERO = 17;
	
	/**
	 * 获取角色必要数据（登录数据打包）
	 * 请求:{@code Request}
	 * 返回:{@code ActorNecessaryDataResponse}
	 */
	byte GET_ACTOR_NECESSARY_DATA = 18;
	
}
