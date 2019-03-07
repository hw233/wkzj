package com.jtang.gameserver.module.adventures.vipactivity.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.vipactivity.model.FailReward;

/**
 * 仙人合成结果返回
 * @author 0x737263
 *
 */
public class HeroComposeResultResponse extends IoBufferSerializer {
	
	/**
	 * 是否成功  0.失败 1.成功
	 */
	public int sucess;
	
	/**
	 * 合成后的仙人id
	 */
	public int heroId;
	
	/**
	 * 合成次数
	 */
	public int composeNum;
	
	/**
	 * 合成失败奖励物品
	 */
	public List<FailReward> failRewards;
	
	public HeroComposeResultResponse(int heroId, int composeNum, List<FailReward> failRewards) {
		this.sucess = heroId > 0 ? 1 : 0;
		this.heroId = heroId;
		this.composeNum = composeNum;
		this.failRewards = failRewards;
	}

	@Override
	public void write() {
		writeInt(this.sucess);
		writeInt(this.heroId);
		writeInt(this.composeNum);
		writeShort((short)failRewards.size());
		
		for (FailReward failReward : failRewards) {
			writeBytes(failReward.getBytes());
		}
	}
}
