package com.jtang.gameserver.module.extapp.invite.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 接受玩家邀请
 * @author ligang
 *
 */
public class TargetInviteRequest extends IoBufferSerializer {

	/**
	 * 好友邀请码
	 */
	public String inviteCode;

	public TargetInviteRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.inviteCode = readString();
	}
	

}
