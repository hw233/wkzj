package com.jtang.gameserver.module.user.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 存储百度云推送服务key
 * @author 0x737263
 *
 */
public class SavePushKeyRequest extends IoBufferSerializer {

	/**
	 * 1.android  2.ios
	 */
	public int type;
	
	/**
	 * pushkey
	 */
	public String pushKey;
	
	public SavePushKeyRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		type = readInt();
		pushKey = readString();
	}

}
