package com.jtang.gameserver.module.app.handler.request;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 领取奖励
 * @author ludd
 *
 */
public class GetRewardRequest extends IoBufferSerializer {
	/**
	 * 活动id
	 */
	public long appId;
	
	/**
	 * 额外参数
	 * key: {@code AppKey}
	 */
	public Map<String, String> paramsMaps;
	
	public GetRewardRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {	
		this.appId = readLong();		
		int size = readShort();
		paramsMaps = new HashMap<String, String>();
		for (int i = 0; i < size; i++) {
			String key = readString();
			String value = readString();
			paramsMaps.put(key, value);
		}
	}
}
