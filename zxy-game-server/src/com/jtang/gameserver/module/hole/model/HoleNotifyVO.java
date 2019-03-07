package com.jtang.gameserver.module.hole.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.TimeUtils;

public class HoleNotifyVO extends IoBufferSerializer {
	
	/**
	 * 盟友id
	 */
	private long allyActorId;
	/**
	 * 洞府自增id
	 */
	public long id;
	
	/**
	 * 洞府id
	 */
	public int holeId;
	
	/**
	 * 结束时间
	 */
	public int endTime;
	
	@Override
	public void write() {
		this.writeLong(this.allyActorId);
		this.writeLong(this.id);
		this.writeInt(this.holeId);
		this.writeInt(this.endTime);
	}
	
	
	
	public HoleNotifyVO(long allyActorId,long id, int holeId, int endTime) {
		super();
		this.allyActorId = allyActorId;
		this.id = id;
		this.holeId = holeId;
		this.endTime = endTime;
	}

	public HoleNotifyVO(String[] str) {
		this.allyActorId = Long.valueOf(str[0]);
		this.id = Long.valueOf(str[1]);
		this.holeId = Integer.valueOf(str[2]);
		this.endTime = Integer.valueOf(str[3]);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(allyActorId);
		sb.append("_");
		sb.append(id);
		sb.append("_");
		sb.append(holeId);
		sb.append("_");
		sb.append(endTime);
		return sb.toString();
	}
	
	public boolean enable() {
		return endTime >= TimeUtils.getNow();
	}
	
	
}
