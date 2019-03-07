package com.jtang.gameserver.module.snatch.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 开始抢夺请求
 * @author liujian
 *
 */
public class StartSnatchRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long targetActorId;
	
	/**
	 * 消息ID
	 */
	public long notifyId;
	
	public StartSnatchRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		targetActorId = this.readLong();
		notifyId = this.readLong();
	}

}
