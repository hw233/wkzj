package com.jtang.gameserver.module.sprintgift.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 领取指定等级礼包
 * @author lg
 *
 */
public class ReceiveSpecifySprintGiftRequest extends IoBufferSerializer {

	
	/**
	 * 指定领取礼包的
	 */
	public int specifyLevel;
	
	@Override
	public void read() {
		specifyLevel = readInt();
	}
	
	public ReceiveSpecifySprintGiftRequest(byte[] bytes) {
		super(bytes);
	}
	
}
