package com.jiatang.common.crossbattle.request;

import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 战斗结果发往世界服务器
 * @author ludd
 *
 */
public class AttackActorResultG2W extends IoBufferSerializer {
	/**
	 * 目标角色id
	 */
	public long targetActorId;
	
	/**
	 * 发起者受伤记录
	 * key:heroId
	 * value：受伤害值
	 */
	public Map<Integer, Integer> actorHurtMap;
	/**
	 * 目标者受伤记录
	 * key:heroId
	 * value：受伤害值
	 */
	public Map<Integer, Integer> targetHurtMap;
	


	public AttackActorResultG2W(long targetActorId, Map<Integer, Integer> actorHurtMap, Map<Integer, Integer> targetHurtMap) {
		super();
		this.targetActorId = targetActorId;
		this.actorHurtMap = actorHurtMap;
		this.targetHurtMap = targetHurtMap;
	}

	


	public AttackActorResultG2W(byte[] bytes) {
		super(bytes);
	}




	@Override
	public void write() {
		this.writeLong(this.targetActorId);
		this.writeIntMap(this.actorHurtMap);
		this.writeIntMap(this.targetHurtMap);
		
	}
	
	@Override
	protected void read() {
		this.targetActorId = readLong();
		this.actorHurtMap = readIntMap();
		this.targetHurtMap = readIntMap();
		
	}

}
