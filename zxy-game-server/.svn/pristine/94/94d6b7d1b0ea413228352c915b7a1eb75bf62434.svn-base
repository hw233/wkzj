package com.jtang.gameserver.module.extapp.invite.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 领取奖励
 * @author ligang
 *
 */
public class InviteRewardRequest extends IoBufferSerializer {
	

	/**
	 *奖励礼包id
	 */
	public byte inviteLevel;

	public InviteRewardRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.inviteLevel = readByte();
	}

}
