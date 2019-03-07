package com.jtang.gameserver.module.adventures.vipactivity.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.vipactivity.model.FailReward;

/**
 * 装备合成结果返回
 * @author 0x737263
 *
 */
public class EquipComposeResultResponse extends IoBufferSerializer {
	
	/**
	 * 是否成功  0.失败 1.成功
	 */
	public int sucess;
	
	/**
	 * 合成后的装备uuid
	 */
	public long uuid;
	
	/**
	 * 合成装备次数
	 */
	public int composeNum;
	
	/**
	 * 合成失败奖励物品
	 */
	public List<FailReward> failRewards;
	
	public EquipComposeResultResponse(long uuid, int composeNum, List<FailReward> failRewards) {
		this.sucess = uuid <= 0 ? 0 : 1;
		this.uuid = uuid;
		this.composeNum = composeNum;
		this.failRewards = failRewards;
	}

	@Override
	public void write() {
		writeInt(this.sucess);
		writeLong(this.uuid);
		writeInt(this.composeNum);

		writeShort((short) failRewards.size());

		for (FailReward failReward : failRewards) {
			writeBytes(failReward.getBytes());
		}
	}
}
