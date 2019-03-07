package com.jiatang.common.crossbattle.model;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiatang.common.crossbattle.type.CrossBattleActorAttributeKey;
import com.jiatang.common.model.DataType;
import com.jtang.core.protocol.IoBufferSerializer;

public class ActorAttributeChangeVO extends IoBufferSerializer {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	/**
	 * 攻击角色id
	 */
	public long acttackActorId;
	/**
	 * 被攻击角色id
	 */
	public long beActtackActorId;
	/**
	 * 攻击方属性变化
	 * key:属性key
	 * value 变化后的值
	 */
	public Map<CrossBattleActorAttributeKey, Number> attackAttsChange;
	
	/**
	 * 被攻击方属性变化
	 * key:属性key
	 * value 变化后的值
	 */
	public Map<CrossBattleActorAttributeKey, Number> beAttackAttsChange;
	
	
	public ActorAttributeChangeVO() {
		super();
	}
	


	public ActorAttributeChangeVO(long acttackActorId, long beActtackActorId,
			Map<CrossBattleActorAttributeKey, Number> attackAttsChange,
			Map<CrossBattleActorAttributeKey, Number> beAttackAttsChange) {
		super();
		this.acttackActorId = acttackActorId;
		this.beActtackActorId = beActtackActorId;
		this.attackAttsChange = attackAttsChange;
		this.beAttackAttsChange = beAttackAttsChange;
	}



	public ActorAttributeChangeVO(byte[] bytes) {
		setReadBuffer(bytes);
	}
	
	
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		this.acttackActorId = buffer.readLong();
		this.beActtackActorId = buffer.readLong();
		this.attackAttsChange = new HashMap<CrossBattleActorAttributeKey, Number>();
		short len = buffer.readShort();
		for (int i = 0; i < len; i++) {
			CrossBattleActorAttributeKey key = CrossBattleActorAttributeKey.getByCode(buffer.readByte());
			Number value = readObject(key.getDataType(), buffer);
			this.attackAttsChange.put(key, value);
		}
		this.beAttackAttsChange = new HashMap<CrossBattleActorAttributeKey, Number>();
		len = buffer.readShort();
		for (int i = 0; i < len; i++) {
			CrossBattleActorAttributeKey key = CrossBattleActorAttributeKey.getByCode(buffer.readByte());
			Number value = readObject(key.getDataType(), buffer);
			this.beAttackAttsChange.put(key, value);
		}
	}
	
	@Override
	public void write() {
		this.writeLong(this.acttackActorId);
		this.writeLong(this.beActtackActorId);
		this.writeShort((short) this.attackAttsChange.size());
		for (Map.Entry<CrossBattleActorAttributeKey, Number> entry : this.attackAttsChange.entrySet()) {
			this.writeByte(entry.getKey().getCode());
			this.writeObject(entry.getValue());
		}
		this.writeShort((short) this.beAttackAttsChange.size());
		for (Map.Entry<CrossBattleActorAttributeKey, Number> entry : this.beAttackAttsChange.entrySet()) {
			this.writeByte(entry.getKey().getCode());
			this.writeObject(entry.getValue());
		}
	}
	
	public Number readObject(DataType dataType, IoBufferSerializer buffer) {
		Number number = null;
		switch (dataType) {
		case BYTE:
			number = buffer.readByte();
			break;
		case SHORT:
			number = buffer.readShort();
			break;
		case INT:
			number = buffer.readInt();
			break;
		case LONG:
			number = buffer.readLong();
			break;

		default:
			LOGGER.error("不支持的数据类型,type:" + dataType);
			break;
		}
		
		return number;
	}
}
