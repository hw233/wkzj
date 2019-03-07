package com.jtang.gameserver.module.adventures.vipactivity.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class GiveEquipInfoResponse extends IoBufferSerializer {

	/**
	 * 是否已经使用 0.未使用 1.已使用
	 */
	public int isUse;
	
	
	/**
	 * 收礼人名称
	 */
	public String name;
	
	/**
	 *等级 
	 */
	public int level;
	
	/**
	 * 第一个英雄id
	 */
	public int firstHeroId;
	
	/**
	 * @param isUse 0.未使用 1.使用
	 * @param name 被赠送玩家名称
	 * @param level 被赠送玩家等级
	 * @param firstHeroId 被赠送玩家第一个仙人id
	 */
	public GiveEquipInfoResponse(int isUse,String name,int level,int firstHeroId){
		this.isUse = isUse;
		this.name = name;
		this.level = level;
		this.firstHeroId = firstHeroId;
	}
	
	public GiveEquipInfoResponse() {
		isUse = 0;
		name = "";
		level = 0;
		firstHeroId = 0;
	}

	@Override
	public void write() {
		writeInt(isUse);
		writeString(name);
		writeInt(level);
		writeInt(firstHeroId);
	}

}
