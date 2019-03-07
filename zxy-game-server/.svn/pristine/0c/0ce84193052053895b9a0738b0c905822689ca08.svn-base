package com.jtang.gameserver.module.delve.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.protocol.IoBufferSerializer;

public class DelveResult extends IoBufferSerializer {

	/**
	 * 潜修结果
	 */
	public Map<HeroVOAttributeKey, DoDelveResult> result = new HashMap<>();
	
	public DelveResult(Map<HeroVOAttributeKey, DoDelveResult> result){
		this.result = result;
	}
	
	public DelveResult() {
	}

	@Override
	public void write() {
		writeShort((short)result.size());
		for(Entry<HeroVOAttributeKey, DoDelveResult> entry:result.entrySet()){
			writeByte(entry.getKey().getCode());
			writeBytes(entry.getValue().getBytes());
		}
	}
}
