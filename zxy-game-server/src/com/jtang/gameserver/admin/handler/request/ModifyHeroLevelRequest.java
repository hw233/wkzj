package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 修改仙人等级请求
 * @author ludd
 *
 */
public class ModifyHeroLevelRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	/**
	 * 仙人id
	 */
	public int heroId;
	/**
	 * 目标等级
	 */
	public int targetLevel;
	
	public ModifyHeroLevelRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.heroId = readInt();
		this.targetLevel = readInt();
	}

}
