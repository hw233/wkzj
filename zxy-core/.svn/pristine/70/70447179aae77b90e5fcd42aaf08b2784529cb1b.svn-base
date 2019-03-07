package com.jiatang.common.crossbattle.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

public class HomeServerRank extends IoBufferSerializer {
	
	/**
	 * 自己排名
	 */
	private int selfRank;
	
	private List<HomeServerRankVO> list;

	
	public HomeServerRank(int selfRank, List<HomeServerRankVO> list) {
		super();
		this.selfRank = selfRank;
		this.list = list;
	}

	public HomeServerRank() {
		super();
	}

	@Override
	public void write() {
		this.writeInt(this.selfRank);
		this.writeShort((short) list.size());
		for (HomeServerRankVO homeRankVO : list) {
			this.writeBytes(homeRankVO.getBytes());
		}
	}
	
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		this.selfRank = buffer.readInt();
		short len = buffer.readShort();
		list = new ArrayList<>();
		for (int i = 0; i < len; i++) {
			HomeServerRankVO homeRankVO = new HomeServerRankVO();
			homeRankVO.readBuffer(buffer);
			list.add(homeRankVO);
		}
	}
}
