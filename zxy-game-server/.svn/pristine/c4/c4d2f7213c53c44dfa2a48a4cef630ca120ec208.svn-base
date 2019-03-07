package com.jtang.gameserver.module.dailytask.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 请求获取任务奖励
 * @author ludd
 *
 */
public class GetDailyTaskRewardRequest extends IoBufferSerializer {
	/**
	 * 获取奖励的任务ID
	 */
	public int taskId;

	public GetDailyTaskRewardRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	protected void read() {
		this.taskId = this.readInt();
	}
}
