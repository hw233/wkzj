package com.jtang.gameserver.module.power.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.icon.model.IconVO;

/**
 * 最强势力对象
 * @author 0x737263
 *
 */
public class PowerVO extends IoBufferSerializer {
	/**
	 * 名次
	 */
	private int rank;
	
	/**
	 * 角色Id
	 */
	private long actorId;
	
	/**
	 * 角色等级
	 */
	private int actorLevel;
	
	/**
	 * 角色名
	 */
	private String actorName;
	
	/**
	 * vip等级
	 * */
	private int vipLevel;
	
	/**
	 * 角色头像
	 * @param packet
	 */
	private IconVO iconVO;
	
	/**
	 * 角色战斗力
	 */
	private int power;

	
	
	
	public PowerVO(int rank, long actorId, int actorLevel, String actorName,
			int vipLevel, IconVO iconVO,int power) {
		super();
		this.rank = rank;
		this.actorId = actorId;
		this.actorLevel = actorLevel;
		this.actorName = actorName;
		this.vipLevel = vipLevel;
		this.iconVO = iconVO;
		this.power = power;
	}


	@Override
	public void write() {
		this.writeInt(rank);
		this.writeLong(actorId);
		this.writeInt(actorLevel);
		this.writeString(actorName);
		this.writeInt(vipLevel);
		this.writeBytes(iconVO.getBytes());
		this.writeInt(power);
	}
	
	
}
