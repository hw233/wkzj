package com.jtang.gameserver.module.user.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 角色登陆请求
 * @author 0x737263
 *
 */
public class ActorLoginRequest extends IoBufferSerializer {
	
	/**
	 * 选择的游戏服Id
	 */
	public int serverId;
	
	/**
	 * 角色Id
	 */
	public long actorId;
	
	/**
	 * sim信息
	 */
	public String sim;

	/**
	 * mac地址
	 */
	public String mac;

	/**
	 * imei信息
	 */
	public String imei;
		
	
	public ActorLoginRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.serverId = readInt();
		this.actorId = readLong();
		this.sim = readString();
		this.mac = readString();
		this.imei = readString();
	}

}
