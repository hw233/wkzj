package com.jtang.gameserver.module.trialcave.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 请求试炼
 * @author lig
 *
 */
public class TrialBattleRequest extends IoBufferSerializer {
	
	/**
	 * 关卡id
	 */
	public byte entranceId;

	@Override
	public void read() {
		this.entranceId = readByte();
	}
	
	public TrialBattleRequest(byte[] data) {
		super(data);
	}
}
