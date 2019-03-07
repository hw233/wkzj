package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 添加魂魄请求
 * @author ludd
 *
 */
public class AddHeroSoulRequest extends IoBufferSerializer {
	
	/**
	 * 角色id
	 */
	public long actorId;
	/**
	 * 仙人id
	 */
	public int heroId;
	/**
	 * 数量
	 */
	public int num;
	
	public AddHeroSoulRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.actorId = readLong();
		this.heroId = readInt();
		this.num = readInt();
	}

}
