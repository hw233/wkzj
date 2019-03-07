package com.jiatang.common.baseworld.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 注册服务器
 * @author ludd
 *
 */
public class SessionRegisterG2W extends IoBufferSerializer {
	/**
	 * 服务器id
	 */
	public int serverId;
	
	public SessionRegisterG2W(int serverId) {
		this.serverId = serverId;
	}
	
	public SessionRegisterG2W(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.serverId = readInt();
	}
	
	@Override
	public void write() {
		writeInt(this.serverId);
	}

}
