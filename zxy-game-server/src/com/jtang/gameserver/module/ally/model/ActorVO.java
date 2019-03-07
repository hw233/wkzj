package com.jtang.gameserver.module.ally.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.icon.model.IconVO;

public class ActorVO extends IoBufferSerializer {

	
	/**
	 * 玩家id
	 */
	public long actorId;
	
	/**
	 * 玩家名字
	 */
	public String name;
	
	/**
	 * 仙人头像
	 */
	public int fistHero;
	
	/**
	 * 等级
	 */
	public int level;
	
	/**
	 * vip等级
	 */
	public int vipLevel;
	
	/**
	 * 头像
	 */
	public IconVO iconVO;
	
	public ActorVO(long actorId,String name,int fistHero,int level,int vipLevel,IconVO iconVO){
		this.actorId = actorId;
		this.name = name;
		this.fistHero = fistHero;
		this.level = level;
		this.vipLevel = vipLevel;
		this.iconVO = iconVO;
	}
	
	@Override
	public void write() {
		writeLong(actorId);
		writeString(name);
		writeInt(fistHero);
		writeInt(level);
		writeInt(vipLevel);
		writeBytes(iconVO.getBytes());
	}
}
