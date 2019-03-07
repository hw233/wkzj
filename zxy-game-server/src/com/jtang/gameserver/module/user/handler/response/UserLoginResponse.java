package com.jtang.gameserver.module.user.handler.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.user.platform.PlatformLoginResult;


/**
 * 用户登陆响应对象
 * @author 0x737263
 */
public class UserLoginResponse extends IoBufferSerializer {

	/**
	 * 平台用户的uid;
	 */
	public String uid;
	
	/**
	 * 登陆后的一些扩展参数 
	 */
	public Map<String, String> params = new HashMap<>();
	
	/**
	 * 重连id
	 */
	public String reconnectId; 

	public UserLoginResponse(PlatformLoginResult platformResult, String reconnectId) {
		this.uid = platformResult.uid;
		this.params.putAll(platformResult.params);
		this.reconnectId = reconnectId;
	}
	
	@Override
	public void write() {
		writeString(uid);
		writeShort((short) params.size());
		for (Entry<String, String> entry : params.entrySet()) {
			writeString(entry.getKey());
			writeString(entry.getValue());
		}
		writeString(this.reconnectId);
	}
	
}
