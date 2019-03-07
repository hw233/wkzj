package com.jtang.gameserver.module.love.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.love.model.LoveFightVO;

public class LoveFightInfoResponse extends IoBufferSerializer {

	/**
	 * 战斗信息列表
	 */
	public List<LoveFightVO> fightInfo = new ArrayList<>();
	
	@Override
	public void write() {
		writeShort((short) fightInfo.size());
		for(LoveFightVO vo:fightInfo){
			writeBytes(vo.getBytes());
		}
	}
}
