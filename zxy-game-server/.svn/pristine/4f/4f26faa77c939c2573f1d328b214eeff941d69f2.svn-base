package com.jtang.gameserver.module.lineup.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 装备下阵
 * @author vinceruan
 *
 */
public class UnAssignEquipRequest extends IoBufferSerializer {
	/**
	 * 装备的唯一id
	 */
	public long equipUuid;
	
	@Override
	public void read() {
		this.equipUuid = readLong();
	}
	
	public UnAssignEquipRequest(byte[] bytes) {
		super(bytes);
	}

}
