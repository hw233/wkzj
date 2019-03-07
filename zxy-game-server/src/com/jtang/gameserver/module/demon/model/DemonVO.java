package com.jtang.gameserver.module.demon.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.demon.type.DemonCamp;
import com.jtang.gameserver.module.icon.model.IconVO;

public class DemonVO extends IoBufferSerializer implements Comparable<DemonVO>  {
	/**
	 * 角色id
	 */
	public long actorId;
	/**
	 * 角色名
	 */
	public String actorName;
	
	/**
	 * 角色等级
	 */
	public int level;
	/**
	 * 功勋值
	 */
	public long feats;
	/**
	 * 阵营
	 */
	public DemonCamp demonCamp;
	
	/**
	 * 加入时间(毫秒)（未加入，时间为0）
	 */
	public long joinTime;
	
	/**
	 * 头像边框
	 */
	public IconVO iconVO;



	public DemonVO(long actorId, String actorName, int level, long feats, DemonCamp demonCamp, long joinTime, IconVO iconVO) {
		super();
		this.actorId = actorId;
		this.actorName = actorName;
		this.level = level;
		this.feats = feats;
		this.demonCamp = demonCamp;
		this.joinTime = joinTime;
		this.iconVO = iconVO;
	}



	@Override
	public void write() {
		this.writeLong(this.actorId);
		this.writeString(this.actorName);
		this.writeInt(this.level);
		this.writeLong(this.feats);
		this.writeInt(demonCamp.getCode());
		this.writeLong(this.joinTime);
		this.writeBytes(this.iconVO.getBytes());
	}
	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (actorId ^ (actorId >>> 32));
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DemonVO other = (DemonVO) obj;
		if (actorId != other.actorId)
			return false;
		return true;
	}



	@Override
	public int compareTo(DemonVO o) {
		if (this.feats < o.feats) {
			return -1;
		} else if (this.feats > o.feats) {
			return 1;
		} else {
			return 0;
		}
	}
}
