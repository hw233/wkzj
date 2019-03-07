package com.jtang.gameserver.module.chat.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.gameserver.module.icon.model.IconVO;

public class DemonChatResponse extends ChatResponse {

	/**
	 * boos名字
	 */
	private String boosName;

	/**
	 * 礼包id
	 */
	private List<RewardObject> reward;

	public DemonChatResponse(int msgType, String actorName, long actorId, int level, int vipLevel, String boosName, List<RewardObject> reward,IconVO iconVO) {
		super(msgType, actorName, actorId, level, vipLevel,iconVO);
		this.boosName = boosName;
		this.reward = reward;
	}

	@Override
	public void write() {
		super.write();
		writeString(boosName);
		writeShort((short) reward.size());
		for (RewardObject rewards : reward) {
			writeBytes(rewards.getBytes());
		}
	}

}
