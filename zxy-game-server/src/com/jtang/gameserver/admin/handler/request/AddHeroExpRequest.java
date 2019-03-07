package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class AddHeroExpRequest extends IoBufferSerializer {

	/**
	 * 玩家id
	 */
	public long actorId;

	/**
	 * 仙人id
	 */
	public int heroId;

	/**
	 * 增加经验
	 */
	public int exp;

	public AddHeroExpRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.actorId = readLong();
		this.heroId = readInt();
		this.exp = readInt();
	}

}
