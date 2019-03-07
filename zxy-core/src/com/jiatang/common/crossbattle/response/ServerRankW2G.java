package com.jiatang.common.crossbattle.response;

import com.jiatang.common.crossbattle.model.ServerScoreList;
import com.jtang.core.protocol.IoBufferSerializer;

public class ServerRankW2G extends IoBufferSerializer {
	public ServerScoreList serverRank;

	public ServerRankW2G(byte[] bytes) {
		super(bytes);
	}

	public ServerRankW2G(ServerScoreList serverRank) {
		super();
		this.serverRank = serverRank;
	}
	
	@Override
	protected void read() {
		this.serverRank = new ServerScoreList();
		this.serverRank.readBuffer(this);
	}
	
	@Override
	public void write() {
		this.writeBytes(serverRank.getBytes());
	}
	
}
