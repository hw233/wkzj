package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class FlushDataConfigRequest extends IoBufferSerializer {

	/**
	 * 文件名称
	 */
	public String fileName;
	
	/**
	 * 文件内容
	 */
	public String data;
	
	public FlushDataConfigRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.fileName = readString();
		this.data = readBigString();
	}

}
