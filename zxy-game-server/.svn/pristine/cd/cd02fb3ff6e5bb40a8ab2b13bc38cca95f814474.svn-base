package com.jtang.gameserver.module.adventures.vipactivity.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 返回炼器宗师信息
 * @author 0x737263
 *
 */
public class EquipComposeInfoResponse extends IoBufferSerializer {
	
	/**
	 * 是否能合成 0.不能  1.可以
	 */
	public int canCompose;
	
	public EquipComposeInfoResponse(boolean canCompose) {
		this.canCompose = canCompose ? 1 : 0;
	}

	@Override
	public void write() {
		writeInt(this.canCompose);
	}
}
