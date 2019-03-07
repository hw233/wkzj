package com.jtang.gameserver.admin.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.component.Game;

public class ServerStateResponse extends IoBufferSerializer {

	/**
	 * 服务器状态
	 */
	private int state;
	/**
	 * 服务器id
	 */
	private int serverId = 1;
	/**
	 * 平台类型
	 */
	private String platformString ="";
	/**
	 * 版本
	 */
	private String version;
	/**
	 * 管理ip列表
	 */
	private List<String> adminIpList;
	/**
	 * 维护ip列表
	 */
	private List<String> maintainIpList;
	
	/**
	 * 服务器进程id
	 */
	private long pid;
	
	
	
	public ServerStateResponse() {
		state = Game.maintain.getType();
		serverId = Game.getServerId();
		platformString = Game.getPlatformString();
		version = Game.getVersion();
		adminIpList = Game.getAdminIpList();
		maintainIpList = Game.getMaintainIpList();
		pid = Game.PID;
	}
	@Override
	public void write() {
		this.writeInt(state);
		this.writeInt(serverId);
		this.writeString(platformString);
		this.writeString(version);
		this.writeStringList(adminIpList);
		this.writeStringList(maintainIpList);
		this.writeLong(pid);
	}

}
