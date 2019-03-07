package com.jiatang.common.crossbattle.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

public class CrossData extends IoBufferSerializer {

	/**
	 * key:serverId
	 * value:totalHurt
	 */
	public Map<Integer, Long> hurtMap;
	
	/**
	 * key:serverId
	 * value:角色数据
	 */
	public Map<Integer, List<ActorCrossData>> actorDataMap;
	
	public CrossData() {
		super();
	}
	
	public CrossData(long selfTotalHurt, long targetTotalHurt,
			List<ActorCrossData> selfCamp,
			List<ActorCrossData> targetCamp, int homeServerId,
			int awayServerId) {
		super();
		hurtMap = new HashMap<Integer, Long>();
		hurtMap.put(homeServerId, selfTotalHurt);
		hurtMap.put(awayServerId, targetTotalHurt);
		
		actorDataMap = new HashMap<>();
		actorDataMap.put(homeServerId, selfCamp);
		actorDataMap.put(awayServerId, targetCamp);
	}

	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		hurtMap = new HashMap<Integer, Long>();
		short len = buffer.readShort();
		for (int i = 0; i < len; i++) {
			int key = buffer.readInt();
			long value = buffer.readLong();
			hurtMap.put(key, value);
		}
		len = buffer.readShort();
		actorDataMap = new HashMap<>();
		for (int i = 0; i < len; i++) {
			int key = buffer.readInt();
			List<ActorCrossData> list = new ArrayList<>();
			short listLen = buffer.readShort();
			for (int j = 0; j < listLen; j++) {
				ActorCrossData actorCrossData = new ActorCrossData();
				actorCrossData.readBuffer(buffer);
				list.add(actorCrossData);
			}
			actorDataMap.put(key, list);
		}
	}
	
	@Override
	public void write() {
		this.writeShort((short) this.hurtMap.size());
		for (Map.Entry<Integer, Long> entry : hurtMap.entrySet()) {
			this.writeInt(entry.getKey());
			this.writeLong(entry.getValue());
		}
		
		this.writeShort((short) this.actorDataMap.size());
		for ( Map.Entry<Integer, List<ActorCrossData>> entry: actorDataMap.entrySet()) {
			this.writeInt(entry.getKey());
			List<ActorCrossData> list = entry.getValue();
			this.writeShort((short) list.size());
			for (ActorCrossData actorCrossData : list) {
				this.writeBytes(actorCrossData.getBytes());
			}
		}
	}
	
}
