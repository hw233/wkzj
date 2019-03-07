package com.jtang.gameserver.module.adventures.bable.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.icon.model.IconVO;

public class BableRankVO extends IoBufferSerializer {
	/**
	 * 角色id
	 */
	private long actorId;
	/**
	 * 登天塔id
	 */
	public int bableId;
	/**
	 * 楼层
	 */
	private int floor;
	/**
	 * 星星数
	 */
	private int star;
	/**
	 * 排名
	 */
	public int rank;
	/**
	 * 角色名
	 */
	private String actorName;
	/**
	 * vip等级
	 */
	private int vipLevel;
	
	/**
	 * 角色等级
	 */
	private int actorLevel;
	
	/**
	 * 头像和边框
	 */
	public IconVO iconVO;
	
	
	public BableRankVO(long actorId, int bableId, int floor, int star, int rank, String actorName, int vipLevel, int actorLevel,IconVO iconVO) {
		super();
		this.actorId = actorId;
		this.bableId = bableId;
		this.floor = floor;
		this.star = star;
		this.rank = rank;
		this.actorName = actorName;
		this.vipLevel = vipLevel;
		this.actorLevel = actorLevel;
		this.iconVO = iconVO;
	}


	@Override
	public void write() {
		this.writeLong(this.actorId);
		this.writeByte((byte)this.bableId);
		this.writeInt(this.floor);
		this.writeInt(this.star);
		this.writeInt(this.rank);
		this.writeString(this.actorName);
		this.writeInt(this.vipLevel);
		this.writeInt(this.actorLevel);
		this.writeBytes(iconVO.getBytes());
	}

}
