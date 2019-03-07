package com.jtang.gameserver.module.ally.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 发送给被解除盟友关系的那一方,以便客户端删除对应的盟友数据
 * @author pengzy
 *
 */
public class AllyRemoveResponse extends IoBufferSerializer{

	/**
	 * 盟友Id
	 */
	private long actorId;
	
	public AllyRemoveResponse(long actorId){
		this.actorId = actorId;
	}
	@Override
	public void write() {
		writeLong(actorId);
	}

}
