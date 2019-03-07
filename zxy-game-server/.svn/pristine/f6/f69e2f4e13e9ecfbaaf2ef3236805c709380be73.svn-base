package com.jtang.gameserver.module.icon.hander.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

public class FramUnLockResponse extends IoBufferSerializer {
	
	/**
	 * 解锁的边框
	 */
	public List<Integer> fram = new ArrayList<>();
	
	public FramUnLockResponse(List<Integer> list){
		this.fram = list;
	}
	
	@Override
	public void write() {
		writeIntList(fram);
	}
}
