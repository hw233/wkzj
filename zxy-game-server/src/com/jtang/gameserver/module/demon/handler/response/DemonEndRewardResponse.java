package com.jtang.gameserver.module.demon.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 集众降魔奖励
 * @author ludd
 *
 */
public class DemonEndRewardResponse extends IoBufferSerializer {

	/**
	 * 第一名奖励
	 */
	private List<RewardObject> firstDemonReward = new ArrayList<>();
	
	/**
	 * 功勋值排名奖励
	 */
	private List<RewardObject> featsRankReward = new ArrayList<>();
	
	/**
	 * 获胜正营奖励
	 */
	private List<RewardObject> winCampReward = new ArrayList<>();
	
	/**
	 * 使用点券产生的奖励
	 */
	private List<RewardObject> useTicketReward = new ArrayList<>();
	
	/**
	 * 奖励积分
	 */
	private long rewardScore;
	
	/**
	 * 阵营是否获胜 1:获胜，0：失败
	 */
	private byte isWin;
	
	/**
	 * 最终排名
	 */
	private int rank;
	
	public DemonEndRewardResponse() {
	}
	

	public DemonEndRewardResponse(List<RewardObject> firstDemonReward, List<RewardObject> featsRankReward, List<RewardObject> winCampReward,
			List<RewardObject> useTicketReward, long rewardScore, byte isWin, int rank) {
		super();
		this.firstDemonReward = firstDemonReward;
		this.featsRankReward = featsRankReward;
		this.winCampReward = winCampReward;
		this.useTicketReward = useTicketReward;
		this.rewardScore = rewardScore;
		this.isWin = isWin;
		this.rank = rank;
	}






	@Override
	public void write() {
		
		this.writeShort((short)firstDemonReward.size());
		for (RewardObject rewardObject : firstDemonReward) {
			this.writeBytes(rewardObject.getBytes());
		}
		
		this.writeShort((short)featsRankReward.size());
		for (RewardObject rewardObject : featsRankReward) {
			this.writeBytes(rewardObject.getBytes());
		}
		
		this.writeShort((short)winCampReward.size());
		for (RewardObject rewardObject : winCampReward) {
			this.writeBytes(rewardObject.getBytes());
		}
		
		this.writeShort((short)useTicketReward.size());
		for (RewardObject rewardObject : useTicketReward) {
			this.writeBytes(rewardObject.getBytes());
		}
		
		this.writeLong(this.rewardScore);
		this.writeByte(this.isWin);
		this.writeInt(this.rank);

	}

}
