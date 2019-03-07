package com.jtang.gameserver.module.notify.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 重播战斗录像响应类
 * @author 0x737263
 *
 */
public class ReplayFightVideoResponse extends IoBufferSerializer {

	/**
	 * 详情查看FightData类
	 */
	public byte[] fightData;

	public ReplayFightVideoResponse(byte[] videoData) {
		fightData = videoData;
	}

	@Override
	public void write() {
		writeBytes(fightData);
	}
}
