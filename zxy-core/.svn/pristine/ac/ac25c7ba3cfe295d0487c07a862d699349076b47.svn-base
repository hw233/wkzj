package com.jiatang.common.crossbattle.model;

import com.jtang.core.protocol.IoBufferSerializer;

public class HomeServerRankVO extends IoBufferSerializer {
	/**
	 * 角色名
	 */
	private String actorName;
	/**
	 * 总伤害
	 */
	private long totalHurt;
	/**
	 * 杀人数
	 */
	private int killNum;
	
	public HomeServerRankVO() {
		super();
	}
	
	public HomeServerRankVO(String actorName, long totalHurt, int killNum) {
		super();
		this.actorName = actorName;
		this.totalHurt = totalHurt;
		this.killNum = killNum;
	}
	
	@Override
	public void write() {
		this.writeString(this.actorName);
		this.writeLong(this.totalHurt);
		this.writeInt(this.killNum);
	}
	
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		this.actorName = buffer.readString();
		this.totalHurt = buffer.readLong();
		this.killNum = buffer.readInt();
	}
	
}
