package com.jtang.gameserver.module.demon.handler.response;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 兑换结果
 * @author ludd
 *
 */
public class DemonExchangeResponse extends IoBufferSerializer {

	/**
	 * 当前积分
	 */
	private long demonScore;
	/**
	 * 兑换物品
	 */
	private RewardObject rewardObject;
	
	
	public DemonExchangeResponse(long demonScore, RewardObject rewardObject) {
		super();
		this.demonScore = demonScore;
		this.rewardObject = rewardObject;
	}


	@Override
	public void write() {
		this.writeLong(demonScore);
		this.writeBytes(rewardObject.getBytes());
	}

}
