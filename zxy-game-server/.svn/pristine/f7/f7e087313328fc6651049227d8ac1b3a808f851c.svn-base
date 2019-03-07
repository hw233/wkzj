package com.jtang.gameserver.module.ladder.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.icon.model.IconVO;

public class LadderActorVO extends IoBufferSerializer {

	/**
	 * 排名
	 */
	public int rank;
	
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
	 * 胜场
	 */
	public int winNum;
	
	/**
	 * 负场
	 */
	public int lostNum;
	
	/**
	 * 积分
	 */
	public int score;
	
	@Override
	public void write() {
		writeInt(rank);
		writeBytes(iconVO.getBytes());
		writeLong(actorId);
		writeString(actorName);
		writeInt(vipLevel);
		writeInt(actorLevel);
//		writeInt(winNum);
//		writeInt(lostNum);
		writeInt(score);
	}
	
}
