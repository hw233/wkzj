package com.jtang.gameserver.module.extapp.ernie.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 摇奖信息
 * @author lig
 *
 */
public class ErnieRecordResponse extends IoBufferSerializer {
	
	/**
	 * 活动开始时间
	 */
	public int startTime;
	
	/**
	 * 活动结束时间
	 */
	public int endTime;
	
	/**
	 * 活动描述
	 */
	public String desc;
	/**
	 * 奖励列表
	 */
	private List<RewardObject> rewardPool;
	

	public ErnieRecordResponse(int startTime, int endTime, String desc, List<RewardObject> pool) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.desc = desc;
		this.rewardPool = pool;
	}
	
	@Override
	public void write() {
		this.writeInt(startTime);
		this.writeInt(endTime);
		this.writeString(desc);
		this.writeShort((short) this.rewardPool.size());
		for (RewardObject vo : rewardPool) {
			this.writeBytes(vo.getBytes());
		}
	}
}
