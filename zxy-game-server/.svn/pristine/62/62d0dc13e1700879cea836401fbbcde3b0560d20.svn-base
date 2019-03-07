package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 删除仙人请求
 * @author ludd
 *
 */
public class DeleteHeroRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 仙人id
	 */
	public int heroId;
	
	public DeleteHeroRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.heroId = readInt();

	}

}
