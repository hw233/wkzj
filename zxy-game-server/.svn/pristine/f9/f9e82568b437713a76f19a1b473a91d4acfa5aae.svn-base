package com.jtang.gameserver.admin.handler;

/**
 * 角色相关
 * @author ludd
 *
 */
public interface ActorCmd {
	/**
	 * 角色修改
	 * 请求: {@code ActorModifyRequest}
	 * 响应: {@code Response AdminStatusCodeConstant}
	 */
	byte MODIFY = 1;
	
	/**
	 * 添加金币
	 * 请求:{@code GiveGoldRequest}
	 * 响应: {@code Response}
	 */
	byte ADD_GOLD = 2;
	
	/**
	 * 	赠送vip等级
	 *  请求:{@code GiveVipLevelRequest}
	 *  响应: {@code Response}
	 */
	byte GIVE_VIP_LEVEL = 5;
	/**
	 * 加声望
	 * 请求:{@code AddReputationRequest}
	 * 响应: {@code Response}
	 */
	byte ADD_REPUTATION = 6;
	/**
	 * 请求:{@code DecreaseGoldRequest}
	 * 响应: {@code Response}
	 */
	byte DECREASE_GOLD = 7;
	
	/**
	 * 删除角色活动数据
	 */
	byte DELETE_ACTOR_ACTIVE = 8;
	
	/**
	 * 修改角色的帐号归属
	 * 请求:{@code ActorChangeUidRequest}
	 * 响应:{@code Response}
	 */
	byte CHANGE_ACTOR_UID = 9;
}
