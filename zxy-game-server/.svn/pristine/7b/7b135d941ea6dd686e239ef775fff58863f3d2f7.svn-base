package com.jtang.gameserver.module.love.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 领取礼物回复
 * @author ludd
 *
 */
public class GetMarryGiftResponse extends IoBufferSerializer {
	private List<RewardObject> list;
	
	
	public GetMarryGiftResponse(List<RewardObject> list) {
		super();
		this.list = list;
	}


	@Override
	public void write() {
		this.writeShort((short) list.size());
		for (RewardObject rewardObject : list) {
			this.writeBytes(rewardObject.getBytes());
		}
	}
}
