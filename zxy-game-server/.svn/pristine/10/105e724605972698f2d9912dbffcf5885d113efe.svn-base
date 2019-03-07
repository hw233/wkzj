package com.jtang.gameserver.admin.handler.request;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;

/**
 * 角色修改请求
 * @author ludd
 *
 */
public class ActorModifyRequest extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 修改字段列表
	 * key {@code ActorAttributeKey}}
	 * value {@code String}}
	 */
	public Map<ActorAttributeKey, String> map;

	public ActorModifyRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.actorId = this.readLong();
		short len = this.readShort();
		map = new HashMap<>();
		for (int i = 0; i < len; i++) {
			ActorAttributeKey fieldKey = ActorAttributeKey.getByCode(this.readByte());
			String fieldValue = this.readString().trim();
			map.put(fieldKey, fieldValue);
		}
	}

}
