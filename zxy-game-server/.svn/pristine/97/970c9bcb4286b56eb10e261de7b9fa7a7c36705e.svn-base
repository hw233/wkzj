package com.jtang.gameserver.module.adventures.favor.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 使用福神眷顾中的特权
 * @author pengzy
 *
 */
public class PrivilegeUseRequest extends IoBufferSerializer {
	/**
	 * 1为第一条特权、2为第二条、3...
	 */
	public int privilegeId;
	
	public PrivilegeUseRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		privilegeId = readInt();
	}
	
}
