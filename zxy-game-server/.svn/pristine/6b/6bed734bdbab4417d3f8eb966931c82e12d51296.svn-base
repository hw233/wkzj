package com.jtang.gameserver.module.ally.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.ally.model.ActorVO;

public class GetActorsResponse extends IoBufferSerializer {
	
	public List<ActorVO> list;
	
	
	public GetActorsResponse(List<ActorVO> list){
		this.list = list;
	}
	
	@Override
	public void write() {
		writeShort((short) list.size());
		for(ActorVO actorVO:list){
			writeBytes(actorVO.getBytes());
		}
	}
}
