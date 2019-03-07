package com.jtang.gameserver.module.ally.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.ally.model.AllyVO;
/**
 * 加入新盟友
 * @author pengzy
 *
 */
public class AllyAddResponse extends IoBufferSerializer{

	private AllyVO allyVO;
	public AllyAddResponse(AllyVO allyVO){
		this.allyVO = allyVO;
	}
	
	@Override
	public void write() {
		allyVO.writePacket(this);
	}

}
