package com.jtang.gameserver.module.sysmail.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 邮件请求
 * @author 0x737263
 *
 */
public class SysmailRequest extends IoBufferSerializer {

	/**
	 * 邮件自增id
	 */
	public long sysmailId;
	
	public SysmailRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	protected void read() {
		sysmailId = readLong();
	}

}
