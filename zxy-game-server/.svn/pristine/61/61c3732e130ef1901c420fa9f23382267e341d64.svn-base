package com.jtang.gameserver.module.adventures.achievement.handler;
/**
 * 
 * 成就里面包含两部分数据：
 * 1、已完成的或已领取奖励的
 * 2、需要做历史记录的（即从角色一进入游戏开始记录其完成状态）
 * @author pengzy
 *
 */
public interface AchieveCmd {

	/**
	 * 请求成就列表
	 * 请求：{@code Request}
	 * 回复：{@code AchievementListResponse}
	 */
	byte GET_ACHIVEMENT = 1;
	
	/**
	 * 请求奖励
	 * <pre>
	 * 请求：{@code GetRewardRequest}
	 * 回复：{@code AchieveAttributeResponse}
	 * </pre>
	 */
	byte GET_REWARD = 2;
	
	/**
	 * 推送成就
	 * <pre>
	 * 推送：{@code AchievementResponse}
	 * </pre>
	 */
	byte PUSH_ACHIEVEMENT = 3;
	
	/**
	 * 推送成就的属性改变
	 * <pre>
	 * 推送：@{code AchieveAttributeResponse}
	 * </pre>
	 */
	byte PUSH_ACHIEVE_ATTRIBUTE = 4;
}
