package com.jtang.gameserver.module.ladder.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.ladder.model.LadderFightVO;

public class LadderFightInfoResponse extends IoBufferSerializer {

	/**
	 * 战斗信息列表
	 */
	public List<LadderFightVO> fightInfo = new ArrayList<>();
	
	@Override
	public void write() {
		writeShort((short) fightInfo.size());
		for(LadderFightVO vo:fightInfo){
			writeBytes(vo.getBytes());
		}
	}
}
