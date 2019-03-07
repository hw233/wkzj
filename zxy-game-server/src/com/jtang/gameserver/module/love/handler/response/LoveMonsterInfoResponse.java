package com.jtang.gameserver.module.love.handler.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.love.model.BossStateVO;

public class LoveMonsterInfoResponse extends IoBufferSerializer {

	/**
	 * boss状态列表
	 */
	Map<Integer,BossStateVO> bossMap = new HashMap<>();
	
	public LoveMonsterInfoResponse(Map<Integer,BossStateVO> bossMap){
		this.bossMap = bossMap;
	}
	
	@Override
	public void write() {
		writeShort((short) bossMap.size());
		for (Entry<Integer, BossStateVO> entry : bossMap.entrySet()) {
			writeInt(entry.getKey());
			writeBytes(entry.getValue().getBytes());
		}
	}
}
