package com.jtang.gameserver.module.ladder.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.ladder.model.LadderActorVO;

public class LadderRankResponse extends IoBufferSerializer {

	/**
	 * 排行列表
	 */
	public List<LadderActorVO> rankList = new ArrayList<>();
	
	/**
	 * 自己的排行
	 */
	public int rank;
	
	@Override
	public void write() {
		writeShort((short) rankList.size());
		for(LadderActorVO vo:rankList){
			writeBytes(vo.getBytes());
		}
		writeInt(rank);
	}
}
