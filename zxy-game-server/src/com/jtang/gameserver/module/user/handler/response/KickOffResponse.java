package com.jtang.gameserver.module.user.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 踢人下线
 * @author 0x737263
 *
 */
public class KickOffResponse  extends IoBufferSerializer {

	/**
	 * 踢人状态码
	 */
	public int code;
	
	public KickOffResponse(int code) {
		this.code = code;
	}
	
	@Override
	public void write() {
		writeInt(this.code);
	}

}
