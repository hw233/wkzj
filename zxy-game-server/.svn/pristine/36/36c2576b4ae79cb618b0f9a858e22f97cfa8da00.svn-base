package com.jtang.gameserver.module.praise.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.praise.type.PraiseRewardType;

public class PraiseRewardRequest extends IoBufferSerializer {

	/**
	 * 奖励类型
	 */
	public PraiseRewardType praiseRewardType;
	
	public PraiseRewardRequest(byte[] bytes) {
		super(bytes);
	}
	@Override
	public void read() {
		praiseRewardType = PraiseRewardType.getByType(readInt());
	}

}
