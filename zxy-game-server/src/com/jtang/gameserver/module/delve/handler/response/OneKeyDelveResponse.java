package com.jtang.gameserver.module.delve.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.delve.model.DelveResult;

public class OneKeyDelveResponse extends IoBufferSerializer {
	
	/**
	 * 潜修结果
	 */
	public List<DelveResult> delveResult = new ArrayList<>();
	
	
	public OneKeyDelveResponse(List<DelveResult> delveResult){
		this.delveResult = delveResult;
	}
	
	@Override
	public void write() {
		writeShort((short)delveResult.size());
		for(DelveResult result:delveResult){
			writeBytes(result.getBytes());
		}
	}
}
