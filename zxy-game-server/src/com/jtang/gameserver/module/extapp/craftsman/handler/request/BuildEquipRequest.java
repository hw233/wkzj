package com.jtang.gameserver.module.extapp.craftsman.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 打造装备
 * @author ligang
 *
 */
public class BuildEquipRequest extends IoBufferSerializer {

	/**
	 * 装备id
	 */
	public long uuid;
	
	/**
	 * 是否使用元宝
	 * 1 未使用  2 使用
	 */
	public byte useTicket;
	
	/**
	 * 要打造装备顺序号
	 */
	public byte buildId;

	public BuildEquipRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.uuid = readLong();
		this.useTicket = readByte();
		this.buildId = readByte();
	}
	

}
