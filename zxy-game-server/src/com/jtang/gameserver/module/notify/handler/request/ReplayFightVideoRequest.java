package com.jtang.gameserver.module.notify.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 重播战斗录像请求
 * @author 0x737263
 *
 */
public class ReplayFightVideoRequest extends IoBufferSerializer {

	/**
	 * 通知Id
	 */
	public long notifyId;
	
	public ReplayFightVideoRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		notifyId = readLong();
	}
	
}
