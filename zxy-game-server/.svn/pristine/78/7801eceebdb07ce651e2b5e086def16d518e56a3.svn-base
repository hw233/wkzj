package com.jtang.gameserver.module.adventures.vipactivity.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 设置主力仙人
 * @author pengzy
 *
 */
public class SetMainHeroRequest extends IoBufferSerializer {

	/**
	 * 设置的主力仙人id
	 */
	public int heroId;
	
	/**
	 * 是否使用金币(0:不使用，1：使用)
	 */
	public byte useGold;
	
	public SetMainHeroRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		heroId = readInt();
		useGold = readByte();
	}

}
