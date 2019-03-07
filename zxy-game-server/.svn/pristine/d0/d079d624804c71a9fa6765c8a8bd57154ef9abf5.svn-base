package com.jtang.gameserver.module.love.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.icon.model.IconVO;

public class LoveRankInfo extends IoBufferSerializer {

	/**
	 * 角色1Id
	 */
	private long actorId1;
	
	/**
	 * 角色1等级
	 */
	private int actorLevel1;
	
	/**
	 * 角色1名字
	 */
	private String actorName1;
	
	/**
	 * 角色1vip等级
	 */
	private int vipLevel1;
	
	/**
	 * 角色1头像信息
	 */
	private IconVO iconVO1;
	
	/**
	 * 角色2Id
	 */
	private long actorId2;
	
	/**
	 * 角色2等级
	 */
	private int actorLevel2;
	
	/**
	 * 角色2名字
	 */
	private String actorName2;
	
	/**
	 * 角色2vip等级
	 */
	private int vipLevel2;
	
	/**
	 * 角色2头像信息
	 */
	private IconVO iconVO2;
	
	/**
	 * 排行
	 */
	private int rank;
	
	/**
	 * 战斗力
	 */
	public int power;
	
	
	public LoveRankInfo(Actor actor1,Actor actor2,int vipLevel1,int vipLevel2,IconVO iconVO1,IconVO iconVO2,int rank,int power){
		this.actorId1 = actor1.getPkId();
		this.actorLevel1 = actor1.level;
		this.actorName1 = actor1.actorName;
		this.vipLevel1 = vipLevel1;
		this.iconVO1 = iconVO1;
		this.actorId2 = actor2.getPkId();
		this.actorLevel2 = actor2.level;
		this.actorName2 = actor2.actorName;
		this.vipLevel2 = vipLevel2;
		this.iconVO2 = iconVO2;
		this.rank = rank;
		this.power = power;
	}
	
	@Override
	public void write() {
		writeLong(actorId1);
		writeInt(actorLevel1);
		writeString(actorName1);
		writeInt(vipLevel1);
		writeBytes(iconVO1.getBytes());
		writeLong(actorId2);
		writeInt(actorLevel2);
		writeString(actorName2);
		writeInt(vipLevel2);
		writeBytes(iconVO2.getBytes());
		writeInt(rank);
		writeInt(power);
	}
}
