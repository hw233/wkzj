package com.jtang.gameserver.module.love.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.icon.model.IconVO;

public class LoveFightVO extends IoBufferSerializer {

	/**
	 * 头像、边框
	 */
	public IconVO iconVO;
	
	/**
	 * 玩家id
	 */
	public long actorId;
	
	/**
	 * 名字
	 */
	public String actorName;
	
	/**
	 * vip等级
	 */
	public int vipLevel;
	
	/**
	 * 玩家等级
	 */
	public int actorLevel;
	
	/**
	 * 战斗结果
	 * 1.胜
	 * 2.负
	 */
	public int fightResult;
	
	/**
	 * 战斗录像id
	 */
	public long fightVideoId;
	
	/**
	 * 战斗时间
	 */
	public int fightTime;
	
	@Override
	public void write() {
		writeBytes(iconVO.getBytes());
		writeLong(actorId);
		writeString(actorName);
		writeInt(vipLevel);
		writeInt(actorLevel);
		writeInt(fightResult);
		writeLong(fightVideoId);
		writeInt(fightTime);
	}
}
