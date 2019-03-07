package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 添加成就
 * @author ludd
 *
 */
public class AddAchievementRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;

	/**
	 * 成就id
	 */
	public int achieveId;
	
	public AddAchievementRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.achieveId = readInt();
	}

}
