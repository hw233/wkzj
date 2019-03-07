package com.jtang.gameserver.module.demon.handler.response;

import java.util.Map;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.protocol.IoBufferSerializer;
/**
 * boss属性变化协议
 * @author ludd
 *
 */
public class BossAttChangeResponse extends IoBufferSerializer {
	/**
	 * 攻击boss的玩家角色id
	 */
	private long attackerActorId;
	/**
	 * 属性变更数据
	 */
	private Map<AttackerAttributeKey, Integer> attributes;
	
	
	public BossAttChangeResponse(long attackerActorId, Map<AttackerAttributeKey, Integer> attributes) {
		super();
		this.attackerActorId = attackerActorId;
		this.attributes = attributes;
	}


	@Override
	public void write() {
		this.writeLong(attackerActorId);
		this.writeShort((short) attributes.size());
		for (Map.Entry<AttackerAttributeKey, Integer> entry : attributes.entrySet()) {
			this.writeByte(entry.getKey().getCode());
			this.writeInt(entry.getValue());
		}
	}

}
