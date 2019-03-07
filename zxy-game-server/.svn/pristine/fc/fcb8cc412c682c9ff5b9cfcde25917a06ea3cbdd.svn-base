package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 添加所有仙人魂魄请求协议
 * @author lig
 *
 */
public class AddAllHeroSoulRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	public AddAllHeroSoulRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = this.readLong();
	}

}
