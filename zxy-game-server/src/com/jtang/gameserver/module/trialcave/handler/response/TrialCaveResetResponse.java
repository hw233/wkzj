package com.jtang.gameserver.module.trialcave.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 获取试炼洞信息
 * @author lig
 *
 */
public class TrialCaveResetResponse extends IoBufferSerializer {	
		
	/**
	 * 今天已经重置试炼的次数
	 */
	private byte trialedResetCount;
	@Override
	public void write() {
		this.writeByte(trialedResetCount);
	}
	
	public TrialCaveResetResponse(int count) {
		this.trialedResetCount = (byte)count;
	}
}
