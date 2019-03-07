package com.jtang.gameserver.module.snatch.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 推送抢夺属性变更
 * @author vinceruan
 *
 */
public class PushSnatchAttributeResponse extends IoBufferSerializer {

	/**
	 * 积分
	 * @param map
	 */
	public int score;
	
	public PushSnatchAttributeResponse(int score) {
		this.score = score;
	}

	@Override
	public void write() {
		writeInt(score);
	}
	

}
