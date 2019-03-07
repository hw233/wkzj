package com.jtang.gameserver.module.crossbattle.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jiatang.common.crossbattle.model.ActorCrossData;
import com.jiatang.common.crossbattle.model.CrossData;
import com.jtang.core.protocol.IoBufferSerializer;

public class CrossDataVO extends IoBufferSerializer {

	/**
	 * key:serverId
	 * value:totalHurt
	 */
	private Map<Integer, Long> hurtMap;
	
	/**
	 * key:serverId
	 * value:角色数据
	 */
	private Map<Integer, List<ActorCrossVO>> actorDataMap;
	
	public CrossDataVO(CrossData crossData) {
		super();
		hurtMap = crossData.hurtMap;
		actorDataMap = new HashMap<>();
		for (Entry<Integer, List<ActorCrossData>> entry : crossData.actorDataMap.entrySet()) {
			int key = entry.getKey();
			List<ActorCrossVO> list = new ArrayList<>();
			List<ActorCrossData> entryValue = entry.getValue();
			for (ActorCrossData actorCrossData : entryValue) {
				ActorCrossVO actorCrossVO = new ActorCrossVO(actorCrossData);
				list.add(actorCrossVO);
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
		for ( Map.Entry<Integer, List<ActorCrossVO>> entry: actorDataMap.entrySet()) {
			this.writeInt(entry.getKey());
			List<ActorCrossVO> list = entry.getValue();
			this.writeShort((short) list.size());
			for (ActorCrossVO actorCrossData : list) {
				this.writeBytes(actorCrossData.getBytes());
			}
		}
	}
}
