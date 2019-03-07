package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 删除魂魄
 * @author ludd
 *
 */
public class DeleteHeroSoulRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 仙人id
	 */
	public int heroId;
	/**
	 * 删除数量
	 */
	public int num;
	
	public DeleteHeroSoulRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.heroId = readInt();
		this.num = readInt();
	}

}
