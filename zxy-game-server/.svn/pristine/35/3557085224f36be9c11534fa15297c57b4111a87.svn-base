package com.jtang.gameserver.module.lineup.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 装备上阵
 * @author vinceruan
 *
 */
public class AssignEquipRequest extends IoBufferSerializer {
	/**
	 * 装备的唯一id
	 */
	public long equipUuid;
	
	/**
	 * 装备上面头像中的索引(从1开始)
	 */
	public int headIndex;
	
	
	public AssignEquipRequest(byte[] data) {
		super(data);
	}
	
	@Override
	public void read() {
		equipUuid = readLong();
		headIndex = readByte();
	}

}
