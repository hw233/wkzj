package com.jtang.gameserver.module.extapp.randomreward.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class GetRewardRequest extends IoBufferSerializer {


	/**
	 * 请求的人物id
	 */
	public int id;
	
	public GetRewardRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		id = readInt();
	}
}
