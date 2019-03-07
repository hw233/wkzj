package com.jiatang.common.crossbattle.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class AttackPlayerW2G extends IoBufferSerializer {
	
	/**
	 * 目标角色id
	 */
	public long targetActorId;
	
	/**
	 * 发起者战斗模型
	 */
	public byte[] selfLineupFightModel;
	
	/**
	 * 目标角色战斗模型
	 */
	public byte[] targetLineupFightModel;
	
	/**
	 * 气势
	 */
	public int selfMorale;
	/**
	 * 目标气势
	 */
	public int targetMorale;
	
	public AttackPlayerW2G(byte[] bytes) {
		super(bytes);
	}
	
	
	
	public AttackPlayerW2G(long targetActorId, byte[] selfLineupFightModel, byte[] targetLineupFightModel, int selfMorale,
			int targetMorale) {
		super();
		this.targetActorId = targetActorId;
		this.selfLineupFightModel = selfLineupFightModel;
		this.targetLineupFightModel = targetLineupFightModel;
		this.selfMorale = selfMorale;
		this.targetMorale = targetMorale;
	}



	@Override
	public void read() {
		this.targetActorId = readLong();
		this.selfLineupFightModel = readByteArray();
		this.targetLineupFightModel = readByteArray();
		this.selfMorale = readInt();
		this.targetMorale = readInt();
	}
	
	@Override
	public void write() {
		this.writeLong(this.targetActorId);
		this.writeByteAarry(this.selfLineupFightModel);
		this.writeByteAarry(this.targetLineupFightModel);
		this.writeInt(this.selfMorale);
		this.writeInt(this.targetMorale);
	}

}
