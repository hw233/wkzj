package com.jtang.gameserver.module.demon.handler.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 功勋值变化协议
 * @author ludd
 *
 */
public class FeatsChangeResponse extends IoBufferSerializer {

	private Map<Long, Long> map = new HashMap<Long, Long>();
	
	
	
	public FeatsChangeResponse(Map<Long, Long> map) {
		super();
		this.map = map;
	}
	
	public FeatsChangeResponse() {
	}
	
	public void addValue(long actorId, Long feats){
		map.put(actorId, feats);
	}



	@Override
	public void write() {
		this.writeShort((short) map.size());
		for (Entry<Long, Long> entry : map.entrySet()) {
			this.writeLong(entry.getKey());
			this.writeLong(entry.getValue());
		}
	}

}
