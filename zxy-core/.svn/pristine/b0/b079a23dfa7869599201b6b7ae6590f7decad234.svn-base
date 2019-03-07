package com.jiatang.common.crossbattle.response;

import com.jiatang.common.crossbattle.model.HomeServerRank;
import com.jtang.core.protocol.IoBufferSerializer;

public class HomeServerRankW2G extends IoBufferSerializer {
	
	public HomeServerRank homeServerRank;

	public HomeServerRankW2G(byte[] bytes) {
		super(bytes);
	}

	public HomeServerRankW2G(HomeServerRank homeServerRank) {
		super();
		this.homeServerRank = homeServerRank;
	}
	
	@Override
	protected void read() {
		this.homeServerRank = new HomeServerRank();
		this.homeServerRank.readBuffer(this);
	}
	
	@Override
	public void write() {
		this.writeBytes(homeServerRank.getBytes());
	}
	
}	
