package com.jiatang.common.crossbattle.response;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

public class TotalHurtW2G extends IoBufferSerializer {
	
	/**
	 * key：服务器id
	 * value：总伤害
	 */
	public Map<Integer, Long> hurtMap;

	public TotalHurtW2G(byte[] bytes) {
		super(bytes);
	}
	
	
	public TotalHurtW2G(Map<Integer, Long> hurtMap) {
		super();
		this.hurtMap = hurtMap;
	}


	@Override
	protected void read() {
		hurtMap = new HashMap<Integer, Long>();
		short len = readShort();
		for (int i = 0; i < len; i++) {
			hurtMap.put(readInt(), readLong());
		}
	}
	
	@Override
	public void write() {
		this.writeShort((short) this.hurtMap.size());
		for (Map.Entry<Integer, Long> entry : hurtMap.entrySet()) {
			this.writeInt(entry.getKey());
			this.writeLong(entry.getValue());
		}
	}
	
}
