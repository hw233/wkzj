package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class AddUidRequest extends IoBufferSerializer {

	/**
	 * uid
	 */
	public String uid;
	
	public AddUidRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.uid = readString();
	}
}
