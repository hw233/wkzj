package com.jtang.gameserver.admin.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 开关服务器
 * @author ludd
 *
 */
public class OpenCloseServerRequest extends IoBufferSerializer {

	/**
	 * 0.正常状态（所有人可访问） 1.维护状态（允许ip列表访问），2,关闭状态（所有人不可访问）
	 */
	public byte flag;
	/**
	 * 延迟踢出玩家（秒）
	 */
	public int time;
	
	public OpenCloseServerRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		flag = readByte();
		time = readInt();
	}

}
