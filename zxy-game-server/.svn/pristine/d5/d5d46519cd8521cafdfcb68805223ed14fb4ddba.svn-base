package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 聚仙阵等级修改
 * @author ludd
 *
 */
public class ModifyRecruitLevelRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 目标等级
	 */
	public int targetLevel;
	
	public ModifyRecruitLevelRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = readLong();
		this.targetLevel = readInt();

	}

}
