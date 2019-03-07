package com.jtang.gameserver.module.user.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 获取角色请求
 * @author 0x737263
 *
 */
public class GetActorRequest extends IoBufferSerializer {

	/**
	 * 选择的游戏服Id
	 */
	public int serverId;
	
	/**
	 * 渠道id(用于记录新进用户)
	 */
	public int channelId;
	
	public GetActorRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.serverId = readInt();
		this.channelId = readInt();
	}
}
