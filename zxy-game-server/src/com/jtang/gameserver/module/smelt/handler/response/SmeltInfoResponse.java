package com.jtang.gameserver.module.smelt.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

public class SmeltInfoResponse extends IoBufferSerializer {

	/**
	 * 已拥有的仙人列表
	 */
	public List<Integer> heroIds = new ArrayList<>();
	
	public SmeltInfoResponse(List<Integer> heroIds){
		this.heroIds = heroIds;
	}
	
	@Override
	public void write() {
		writeIntList(heroIds);
	}
}
