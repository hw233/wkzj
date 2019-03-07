package com.jiatang.common.crossbattle.model;

import com.jtang.core.protocol.IoBufferSerializer;

public class AttackPlayerResultVO extends IoBufferSerializer{
	
	/**
	 * 基本伤害
	 */
	private int baseHurt;
	/**
	 * 额外伤害
	 */
	private int extHurt;
	
	/**
	 * 杀人数
	 */
	private int killNum;
	
	/**
	 * 连杀数
	 */
	private int continueKillNum;
	
	public AttackPlayerResultVO() {
	}

	public AttackPlayerResultVO(int baseHurt, int extHurt, int killNum,
			int continueKillNum) {
		super();
		this.baseHurt = baseHurt;
		this.extHurt = extHurt;
		this.killNum = killNum;
		this.continueKillNum = continueKillNum;
	}
	
	@Override
	public void write() {
		this.writeInt(baseHurt);
		this.writeInt(extHurt);
		this.writeInt(killNum);
		this.writeInt(continueKillNum);
	}
	
	
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		this.baseHurt = buffer.readInt();
		this.extHurt = buffer.readInt();
		this.killNum = buffer.readInt();
		this.continueKillNum = buffer.readInt();
	}

	
}
