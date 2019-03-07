package com.jtang.gameserver.module.crossbattle.model;

import com.jiatang.common.crossbattle.model.ActorCrossData;
import com.jtang.core.protocol.IoBufferSerializer;

public class ActorCrossVO extends IoBufferSerializer {
	/**
	 * 本服排名
	 */
	public int powerRank;
	
	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 角色名
	 */
	public String actorName;
	
	/**
	 * 角色等级
	 */
	public int level;

	/**
	 * 当前血量
	 */
	public int hp;
	
	/**
	 * 气势
	 */
	public int morale;
	
	/**
	 * 最大值(报名时计算好)
	 */
	public int hpMax;
	
	/**
	 * vip等级
	 */
	public int vipLevel;
	
	/**
	 * 死亡时间（秒）
	 */
	public int deadTime;
	
	/**
	 * 连续杀人数
	 */
	public int continueKillNum;
	
	/**
	 * 杀人数
	 */
	public int killNum;
	
	/**
	 * 获得伤害量
	 */
	public long totalHurt;
	
	/**
	 * 攻击时间
	 */
	private int attackTime;
	
	/**
	 * 复活时间
	 */
	private int reviveTime;
	
	public ActorCrossVO() {
	}
	
	public ActorCrossVO(ActorCrossData actorCrossData) {
		super();
		this.powerRank = actorCrossData.powerRank;
		this.actorId = actorCrossData.actorId;
		this.actorName = actorCrossData.actorName;
		this.level = actorCrossData.level;
		this.hp = actorCrossData.hp;
		this.morale = actorCrossData.morale;
		this.hpMax = actorCrossData.hpMax;
		this.vipLevel = actorCrossData.vipLevel;
		this.deadTime = actorCrossData.getDeadTime();
		this.continueKillNum = actorCrossData.getContinueKillNum();
		this.killNum = actorCrossData.getKillNum();
		this.totalHurt = actorCrossData.getRewardHurt();
		this.attackTime = actorCrossData.getAttackTime();
		this.reviveTime = actorCrossData.getReviveTime();
	}

	@Override
	public void write() {
		this.writeInt(this.powerRank);
		this.writeLong(this.actorId);
		this.writeString(this.actorName);
		this.writeInt(this.level);
		this.writeInt(this.hp);
		this.writeInt(this.morale);
		this.writeInt(this.hpMax);
		this.writeInt(this.vipLevel);
		this.writeInt(this.deadTime);
		this.writeInt(this.continueKillNum);
		this.writeInt(this.killNum);
		this.writeLong(this.totalHurt);
		this.writeInt(attackTime);
		this.writeInt(reviveTime);
	}
	
}
