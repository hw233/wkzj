package com.jtang.gameserver.module.notify.handler;

public interface NotifyCmd {

	/**
	 * 角色获取其信息列表
	 * <pre>
	 * 请求：{@code Request}
	 * 回复：{@code AllNotifyResponse}
	 * </pre>
	 */
	byte GET_NOTIFY = 1;
	
	
	/**
	 * 角色领取信息通知附带的奖励，比如试炼的奖励
	 * <pre>
	 * 请求：{@code GetRewardRequest}
	 * 回复：{@code Response}
	 * </pre>
	 */
	byte GET_REWARD = 2;
	
	/**
	 * 抢夺或被抢夺的角色通知盟友
	 * <pre>
	 * 请求：{@code NoticeAllyRequest}
	 * 回复：{@code Response}
	 * </pre>
	 */
	byte NOTICE_ALLY = 3;
	
	/**
	 * 收到通知
	 * 推送：{@code NotifyResponse}
	 */
	byte RECEIVE_NOFITY = 4;
	
	/**
	 * 删除信息
	 * 推送：{@code RemoveNotifyResponse}
	 */
	byte PUSH_REMOVE_NOTIFY = 5;
	
	/**
	 * 删除信息
	 * 请求：{@code RemoveNotifyRequest}
	 * 回复：{@code Response}
	 */
	byte REMOVE_NOTIFY = 6;
	
	/**
	 * 将所有收到的信息设为已读
	 * 请求：{@code Request}
	 * 回复：{@code Response}
	 */
	byte SET_READED = 7;
	
	/**
	 * 重播战斗录像
	 * 请求:{@code ReplayFightVideoRequest}
	 * 回复:{@code ReplayFightVideoResponse}
	 */
	byte REPLAY_FIGHT_VIDEO = 8;
}
