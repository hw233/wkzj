package com.jtang.gameserver.module.chat.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.gameserver.module.icon.model.IconVO;

public class DemonWinResponse extends ChatResponse {
	/**
	 * 集众降魔获胜方所有人奖励
	 */
	public List<RewardObject> firstDemonReward;

	/**
	 * 集众降魔获胜方第一名奖励
	 */
	public List<RewardObject> winCampReward;

	public DemonWinResponse(int msgType, String sendName, long actorId, int level, int vipLevel, List<RewardObject> firstDemonReward,
			List<RewardObject> winCampReward,IconVO iconVO) {
		super(msgType, sendName, actorId, level, vipLevel,iconVO);
		this.firstDemonReward = firstDemonReward;
		this.winCampReward = winCampReward;
	}

	@Override
	public void write() {
		super.write();
		writeShort((short) firstDemonReward.size());
		for (RewardObject reward : firstDemonReward) {
			writeBytes(reward.getBytes());
		}
		writeShort((short) winCampReward.size());
		for (RewardObject reward : winCampReward) {
			writeBytes(reward.getBytes());
		}
	}

}
