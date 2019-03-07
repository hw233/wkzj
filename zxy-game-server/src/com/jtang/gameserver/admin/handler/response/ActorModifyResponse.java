package com.jtang.gameserver.admin.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;
/**
 * 修改角色返回
 * @author ludd
 *
 */
public class ActorModifyResponse extends IoBufferSerializer {

	/**
	 * 失败字段key
	 */
	private List<ActorAttributeKey> list = new ArrayList<>();
	
	
	public ActorModifyResponse(List<ActorAttributeKey> list) {
		super();
		this.list = list;
	}


	@Override
	public void write() {
		this.writeShort((short)list.size());
		for (ActorAttributeKey key : list) {
			this.writeByte(key.getCode());
		}
	}

}
