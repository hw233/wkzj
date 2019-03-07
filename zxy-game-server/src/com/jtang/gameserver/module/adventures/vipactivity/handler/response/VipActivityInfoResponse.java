package com.jtang.gameserver.module.adventures.vipactivity.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class VipActivityInfoResponse extends IoBufferSerializer {

	/**
	 * 装备合成次数
	 */
	public int equipComposeNum;
	
	/**
	 * 仙人合成次数
	 */
	public int heroComposeNum;
	
	/**
	 * 重置次数
	 */
	public int resetNum;
	
	public VipActivityInfoResponse(int equipComposeNum,int heroComposeNum,int resetNum){
		this.equipComposeNum = equipComposeNum;
		this.heroComposeNum = heroComposeNum;
		this.resetNum = resetNum;
	}
	
	@Override
	public void write() {
		writeInt(equipComposeNum);
		writeInt(heroComposeNum);
		writeInt(resetNum);
	}
}
