package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 删除成就
 * @author ludd
 *
 */
public class DeleteAchievementRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	/**
	 * 成就id
	 */
	public int achieveId;
	
	public DeleteAchievementRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.achieveId = readInt();
	}

}
