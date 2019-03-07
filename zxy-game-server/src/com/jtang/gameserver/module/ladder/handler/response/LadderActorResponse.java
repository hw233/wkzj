package com.jtang.gameserver.module.ladder.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.ladder.model.LadderActorVO;

public class LadderActorResponse extends IoBufferSerializer {

	/**
	 * 对手列表
	 */
	public List<LadderActorVO> actorList = new ArrayList<>();
	
	/**
	 * 下次刷新需要的金币
	 */
	public int costGold;
	
	@Override
	public void write() {
		writeShort((short) actorList.size());
		for(LadderActorVO vo:actorList){
			writeBytes(vo.getBytes());
		}
		writeInt(costGold);
	}
}
