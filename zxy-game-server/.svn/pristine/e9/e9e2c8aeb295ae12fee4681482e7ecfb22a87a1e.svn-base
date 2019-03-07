package com.jtang.gameserver.module.adventures.achievement.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 领取成就奖励
 * @author pengzy
 *
 */
public class GetRewardRequest extends IoBufferSerializer {
	/**
	 * 成就Id
	 */
	public int achieveId;
	
	public GetRewardRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		achieveId = readInt();
	}

}
