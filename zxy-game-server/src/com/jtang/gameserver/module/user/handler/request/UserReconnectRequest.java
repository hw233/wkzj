package com.jtang.gameserver.module.user.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 用户重连请求
 * @author 0x737263
 *
 */
public class UserReconnectRequest extends IoBufferSerializer {

	/**
	 * 平台id
	 */
	public int platformId;
	/**
	 * 连接id
	 */
	public String connectionId;
	
	/**
	 * 版本号
	 */
	public String version;
	
	
	public UserReconnectRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.platformId = readInt();
		this.connectionId = readString();
		this.version = readString();
	}
	
	public boolean mathVersion(String version) {
		if(this.version.isEmpty()) {
			return false;
		}
		return this.version.equals(version);
	}

}
