package com.jtang.gameserver.module.delve.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 重修：将Hero的属性以及潜修次数回复到上次潜修的状态
 * @author pengzy
 *
 */
public class RepeatDelveRequest extends IoBufferSerializer {
	/**
	 * 仙人Id
	 */
	public int heroId;
	
	public RepeatDelveRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		heroId = readInt();
	}

}
