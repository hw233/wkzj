package com.jtang.gameserver.module.user.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 创建角色请求
 * @author 0x737263
 *
 */
public class CreateActorRequest extends IoBufferSerializer {

	/**
	 * 选择的游戏服Id
	 */
	public int serverId;
	
	/**
	 * 选择的仙人Id
	 */
	public int heroId;
	
	/**
	 * 输入角色名
	 */
	public String actorName;
	
	/**
	 * 创建角色时的渠道id
	 */
	public int channelId;
	
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
	
	public CreateActorRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.serverId = readInt();
		this.heroId = readInt();
		this.actorName = readString();
		this.channelId = readInt();
		this.sim = readString();
		this.mac = readString();
		this.imei = readString();
	}
}
