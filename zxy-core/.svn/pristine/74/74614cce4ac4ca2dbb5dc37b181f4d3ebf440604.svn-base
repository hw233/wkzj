package com.jiatang.common.crossbattle.model;

import com.jtang.core.protocol.IoBufferSerializer;

public class ServerScoreVO extends IoBufferSerializer {
	
	/**
	 * 分组id
	 */
	private int groupId;
	/**
	 * 服务器id
	 */
	private int serverId;
	/**
	 * 角色名
	 */
	private String actorName;
	/**
	 * 联赛积分
	 */
	private int score;
	
	
	public ServerScoreVO() {
		super();
	}
	public ServerScoreVO(int groupId,int serverId, String actorName, int score) {
		super();
		this.groupId = groupId;
		this.serverId = serverId;
		this.actorName = actorName;
		this.score = score;
	}
	
	@Override
	public void write() {
		this.writeInt(this.groupId);
		this.writeInt(this.serverId);
		this.writeString(actorName);
		this.writeInt(this.score);
	}
	
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		this.groupId = buffer.readInt();
		this.serverId = buffer.readInt();
		this.actorName = buffer.readString();
		this.score = buffer.readInt();
	}
	
	
}
