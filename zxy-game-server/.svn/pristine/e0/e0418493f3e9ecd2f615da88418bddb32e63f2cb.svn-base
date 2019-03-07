package com.jtang.gameserver.module.extapp.invite.handler.response;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

public class InviteResponse extends IoBufferSerializer {

	/**
	 * 邀请码
	 */
	public String inviteCode;
	
	/**
	 * 是否已被邀请
	 * 1 未被邀请过  2 已经被邀请过
	 */
	public int isInvite;
	
	/**
	 * 已经邀请的人名
	 */
	public String inviteName;
	
	/**
	 * 奖励领取情况
	 * 格式Map<邀请人数, 礼包状态>
	 * status : 1 未达到条件、2 达到条件未领取、3已领取
	 */
	public Map<Integer,Integer> rewardMap = new HashMap<Integer,Integer>();
	
	
	
	public InviteResponse(){
	}
	
	@Override
	public void write() {
		writeString(this.inviteCode);
		writeInt(this.isInvite);
		writeString(this.inviteName);
		writeIntMap(this.rewardMap);
	}
	
}
