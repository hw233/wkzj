package com.jtang.gameserver.module.user.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 用户登陆请求
 * ps:参数临时的，仅用于demo测试
 * @author 0x737263
 */
public class UserLoginRequest extends IoBufferSerializer {
	
	/**
	 * 平台id
	 * 1.嘉堂平台
	 */
	public int platformId;
		
	/**
	 * 令牌字符串 分隔符
	 */
	public String token;
	
	/**
	 * 版本号
	 */
	public String version;
	
	
	public UserLoginRequest(byte[] bytes) {
		super(bytes);
	}
	

	@Override
	public void read() {
		this.platformId = readInt();
		this.token = readString();
		this.version = readString();
	}
	
	public boolean mathVersion(String version) {
		if(this.version.isEmpty()) {
			return false;
		}
		return this.version.equals(version);
	}
}
