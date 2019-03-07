package com.jtang.gameserver.module.delve.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 客户端向服务端请求潜修仙人
 * @author ludd
 *
 */
public class DoDelveRequest extends IoBufferSerializer {

	/**
	 * 仙人Id
	 */
	public int heroId;
	
	/**
	 * 潜修类型
	 */
	public int type;
	
	public DoDelveRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		heroId = readInt();
		type = readByte();
	}

}
