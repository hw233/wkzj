package com.jtang.worldserver.module.base.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class ReloadConfigRequest extends IoBufferSerializer {
	
	/**
	 * 文件名称
	 */
	public String fileName;
	
	/**
	 * 文件内容
	 */
	public String data;
	
	public ReloadConfigRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.fileName = readString();
		this.data = readBigString();
	}


}
