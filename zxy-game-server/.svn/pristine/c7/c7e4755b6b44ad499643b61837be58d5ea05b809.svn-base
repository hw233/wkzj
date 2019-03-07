package com.jtang.gameserver.module.crossbattle.model;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 数据转发对象
 * 主要处理gameserver收到的数据转发到world服务器
 * @author 0x737263
 *
 */
public class Game2WorldForward extends IoBufferSerializer {
	
	public Object[] forwardList;
	
	public Game2WorldForward(Object... forwardData) {
		this.forwardList = forwardData;
	}
	
	@Override
	public void write() {
		for (Object obj : forwardList) {
			if (obj instanceof IoBufferSerializer) {
				writeBytes(((IoBufferSerializer) obj).getBytes());
			}
			if (obj instanceof byte[]) {
				writeBytes((byte[]) obj);
			}
		}
	}

}
