package com.jtang.gameserver.admin.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class ActorEntityResponse extends IoBufferSerializer {

	/**
	 * 实体字符串 （使用bigString，即长度用int接收
	 */
	private String entityJSONString;
	
	
	public ActorEntityResponse(String entityJSONString) {
		super();
		this.entityJSONString = entityJSONString;
	}


	@Override
	public void write() {
		this.writeBigString(entityJSONString);
	}

}
