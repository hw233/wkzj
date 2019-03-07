package com.jiatang.common.crossbattle.model;

import com.jtang.core.protocol.IoBufferSerializer;

public class AttackNoticeVO extends IoBufferSerializer {
	
	/**
	 * 1：主动攻击，0：被动攻击
	 */
	private byte beAttack;
	/**
	 * 目标角色名
	 */
	private String targetActorName;
	
	/**
	 * 获取的伤害
	 */
	private int rewardHurt;
	
	/**
	 * 额外获得的伤害
	 */
	private int extRewardHurt;

	public AttackNoticeVO(byte beAttack, String targetActorName, int rewardHurt,
			int extRewardHurt) {
		super();
		this.beAttack = beAttack;
		this.targetActorName = targetActorName;
		this.rewardHurt = rewardHurt;
		this.extRewardHurt = extRewardHurt;
	}
	
	@Override
	public void write() {
		this.writeByte(this.beAttack);
		this.writeString(this.targetActorName);
		this.writeInt(this.rewardHurt);
		this.writeInt(this.extRewardHurt);
	}
	
	
	
}
