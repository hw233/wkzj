package com.jtang.gameserver.module.hole.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 奖励列表
 * @author jerry
 *
 */
public class HoleRewardResponse extends IoBufferSerializer {
	private List<RewardObject> list;

	public HoleRewardResponse(List<RewardObject> list) {
		super();
		this.list = list;
	};
	
	@Override
	public void write() {
		this.writeShort((short) this.list.size());
		for (RewardObject rewardObject : list) {
			this.writeBytes(rewardObject.getBytes());
		}
	}
}
