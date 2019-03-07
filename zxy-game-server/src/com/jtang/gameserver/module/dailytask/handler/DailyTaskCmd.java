package com.jtang.gameserver.module.dailytask.handler;



public interface DailyTaskCmd {
	/**
	 * 获取任务列表
	 * 请求：@{code Request}
	 * 回复：@{code {@link DailyTaskInfoResponse}}
	 */
	byte GET_DAILY_TASK_INFO = 1;
	
	/**
	 * 获取奖励
	 * 请求：@{code {@link GetDailyTaskRewardRequest}}
	 * 回复:(@code Response}
	 */
	byte GET_REWARD = 2;
	
	/**
	 * 推送每日任务进度
	 * 推送：{@code DailyTaskVO}
	 */
	byte PUSH_DAILY_TASK_PROGRESS = 3;
	/**
	 * 推送每日任务刷新
	 * 推送：{@code Response}
	 */
	byte PUSH_DAILY_TASK_RESET = 4;
}
