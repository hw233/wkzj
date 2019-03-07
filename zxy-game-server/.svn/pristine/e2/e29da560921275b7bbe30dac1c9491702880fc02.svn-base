package com.jtang.gameserver.module.demon.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 攻击玩家请求
 * @author ludd
 *
 */
public class AttackPlayerRequest extends IoBufferSerializer {
	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 是否使用点券 0：不使用， 1：使用
	 */
	public byte useTicket;
	
	public AttackPlayerRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.useTicket = readByte();
	}

}
