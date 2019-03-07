package com.jtang.gameserver.module.ally.handler;
/**
 * 联盟相关的消息指令
 * @author pengzy
 *
 */
public interface AllyCmd {

	/**
	 * 请求盟友列表
	 * <pre>
	 * 请求：{@code Request}
	 * 回复：{@code AllyListResponse}
	 * </pre>
	 */
	byte GET_ALLY_LIST = 1;
	
	/**
	 * 添加盟友请求
	 * <pre>
	 * 请求：{@code AllyAddRequest}
	 * 回复：{@code AllyAddResponse}
	 * </pre>
	 */
	byte ADD_ALLY = 2;
	
	/**
	 * 移除盟友请求
	 * <pre>
	 * 请求：{@code AllyRemoveRequest}
	 * 回复：{@code AllyRemoveResponse}
	 * </pre>
	 */
	byte REMOVE_ALLY = 3;
	
	/**
	 * 推送新盟友给客户端
	 * <pre>
	 * 回复：{@code AllyAddResponse}
	 * </pre>
	 */
	byte PUSH_NEW_ALLY = 4;
	
	/**
	 * 推送删除盟友
	 * <pre>
	 * 回复：{@code AllyRemoveResponse}
	 * </pre>
	 */
	byte PUSH_REMOVE_ALLY = 5;
	
	/**
	 * 盟友切磋
	 * <pre>
	 * 请求：{@code AllyFightRequest}
	 * 回复：{@code AllyFightResponse}如果成功将会返回此回复，此回复的指令为FIGHT_RESULT
	 * </pre>
	 */
	byte ALLY_FIGHT = 6;
	
	/**
	 * 返回盟友切磋结果
	 * <pre>
	 * 回复：{@code AllyFightResponse}
	 * </pre>
	 */
	byte FIGHT_RESULT = 7;
	
	/**
	 * 更新AllyVO的属性
	 * <pre>
	 * 回复：{@code AllyAttributeUpdateResponse}
	 * </pre>
	 */
	byte PUSH_ALLY_ATTRIBUTE = 8;
	
	/**
	 * 更新日可切磋次数
	 * 回复：{@code AllyFightCountResponse}
	 */
	byte PUSH_FIGHT_DAY_COUNT = 10;
	
	/**
	 * 获取角色坐标
	 * 请求：{@code GetCoordinateRequest}
	 * 回复：{@code CoordinateResponse}
	 */
	byte GET_COORDINATE = 11;
	
	/**
	 * 更新坐标
	 * 请求：{@code CoordinateUpdateRequest}
	 * 回复：{@code Response}
	 */
	byte UPDATE_COORDINATE = 12;
	
	/**
	 * 获取一批陌生人
	 * 请求:{@code Request}
	 * 返回:{@code GetActorsResponse}
	 */
	byte GET_ACTORS = 14;
	
	/**
	 * 获取机器人
	 * 请求:{@code Request}
	 * 返回:{@code GetActorResponse}
	 */
	byte GET_ROBOT = 15;

}
