package com.jtang.gameserver.admin.handler.request;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.IoBufferSerializer;

public class SendSysmailRequest extends IoBufferSerializer {

	/**
	 * actorId
	 */
	public long actorId;

	/**
	 * 奖励列表
	 */
	public List<RewardObject> list;

	/**
	 * 文本
	 */
	public String text;

	public SendSysmailRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		actorId = readLong();
		short length = readShort();
		list = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			RewardObject rewardObject = new RewardObject();
			rewardObject.rewardType = RewardType.getType(readInt());
			rewardObject.id = readInt();
			rewardObject.num = readInt();
			list.add(rewardObject);
		}
		text = readString();
	}

}
