package com.jiatang.common.crossbattle.model;

import com.jtang.core.protocol.IoBufferSerializer;

public class EndNoticeVO extends IoBufferSerializer {
	/**
	 * 目标服务器
	 */
	private int targetServerId;
	
	/**
	 * 第一玩家名
	 */
	private String bestActorName;
	
	/**
	 * 第一名玩家等级
	 */
	private int bestActorLevel;
	
	/**
	 * 第一名玩家vip等级
	 */
	private int bestActorVipLevel;
	
	/**
	 * 总共杀人数
	 */
	private int killNum;
	
	/**
	 * 第一名玩家伤害
	 */
	private long rewardHurt;
	
	/**
	 * 最高连杀数
	 */
	private int continueKillNum;
	
	public EndNoticeVO() {
	}

	
	public EndNoticeVO(int targetServerId, String bestActorName,
			int bestActorLevel, int bestActorVipLevel, int killNum,
			long rewardHurt, int continueKillNum) {
		super();
		this.targetServerId = targetServerId;
		this.bestActorName = bestActorName;
		this.bestActorLevel = bestActorLevel;
		this.bestActorVipLevel = bestActorVipLevel;
		this.killNum = killNum;
		this.rewardHurt = rewardHurt;
		this.continueKillNum = continueKillNum;
	}


	@Override
	public void write() {
		this.writeInt(this.targetServerId);
		this.writeString(this.bestActorName);
		this.writeInt(this.bestActorLevel);
		this.writeInt(this.bestActorVipLevel);
		this.writeInt(this.killNum);
		this.writeLong(this.rewardHurt);
		this.writeInt(this.continueKillNum);
	}
	
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		this.targetServerId = buffer.readInt();
		this.bestActorName = buffer.readString();
		this.bestActorLevel = buffer.readInt();
		this.bestActorVipLevel = buffer.readInt();
		this.killNum = buffer.readInt();
		this.rewardHurt = buffer.readLong();
		this.continueKillNum = buffer.readInt();
	}
	
	
}
