package com.jtang.gameserver.module.crossbattle.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.crossbattle.model.CrossDataVO;
/**
 * 联赛数据
 * @author ludd
 *
 */
public class ActorCrossDataResponse extends IoBufferSerializer {
	
	private CrossDataVO crossDataVO;
	
	
	public ActorCrossDataResponse(CrossDataVO crossDataVO) {
		super();
		this.crossDataVO = crossDataVO;
	}


	@Override
	public void write() {
		this.writeBytes(this.crossDataVO.getBytes());
	}
	

}
