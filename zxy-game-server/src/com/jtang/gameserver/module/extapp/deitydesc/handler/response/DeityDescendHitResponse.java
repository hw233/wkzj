package com.jtang.gameserver.module.extapp.deitydesc.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;

public class DeityDescendHitResponse extends IoBufferSerializer {

	/**
	 * 砸蛋奖励物品
	 */
	public List<RewardObject> rewardObjects = new ArrayList<RewardObject>();
	
	/**
	 * 当前已经点亮的字符
	 */
	public byte curIndex;
	
	
	
	public DeityDescendHitResponse(byte index, List<RewardObject> rewardObjects) {
		this.curIndex = index;
		this.rewardObjects = rewardObjects;
	}
	@Override
	public void write() {
		writeShort((short)rewardObjects.size());
		for (RewardObject rewardObject : rewardObjects) {
			writeBytes(rewardObject.getBytes());
		}
		writeByte(this.curIndex);
	}
}
