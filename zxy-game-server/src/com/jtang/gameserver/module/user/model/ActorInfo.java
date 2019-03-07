package com.jtang.gameserver.module.user.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.icon.model.IconVO;

public class ActorInfo extends IoBufferSerializer {
	/**
	 * 角色id
	 */
	public long actorId;
	/**
	 * 角色名
	 */
	public String actorName;
	/**
	 * 等级
	 */
	public int level;
	/**
	 * vip等级
	 */
	public int vipLevel;
	/**
	 * 头像边框信息
	 */
	public IconVO iconVO;
	/**
	 * 上次登出时间
	 */
	public int lastLogoutTime;
	public ActorInfo(long actorId, String actorName, int lastLogoutTime,int level,int vipLevel,IconVO iconVO) {
		super();
		this.actorId = actorId;
		this.actorName = actorName;
		this.level = level;
		this.vipLevel = vipLevel;
		this.iconVO = iconVO;
		this.lastLogoutTime = lastLogoutTime;
	}
	
	
	@Override
	public void write() {
		this.writeLong(this.actorId);
		this.writeString(this.actorName);
		writeInt(this.level);
		writeInt(this.vipLevel);
		writeBytes(iconVO.getBytes());
		this.writeInt(this.lastLogoutTime);
	}
	
}
