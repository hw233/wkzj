package com.jtang.gameserver.module.demon.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.icon.model.IconVO;

/**
 * 排名数据vo
 * @author ludd
 *
 */
public class DemonRankVO extends IoBufferSerializer {
	
	private long actorId;
	/*
	 * 排名
	 */
	private int rank;
	/**
	 * 角色名
	 */
	private String actorName;
	/**
	 * 功勋值
	 */
	private long feats;
	/**
	 * 阵营是否获胜
	 */
	private byte isWin;
	
	/**
	 * 头像边框
	 */
	private IconVO iconVO;
	
	public DemonRankVO(long actorId, String actorName, IconVO iconVO) {
		this.actorId = actorId;
		this.actorName = actorName;
		this.iconVO = iconVO;
	}
	public DemonRankVO(long actorId, int rank, String actorName, long feats, byte isWin, IconVO iconVO) {
		super();
		this.actorId = actorId;
		this.rank = rank;
		this.actorName = actorName;
		this.feats = feats;
		this.isWin = isWin;
		this.iconVO = iconVO;
	}
	@Override
	public void write() {
		this.writeLong(this.actorId);
		this.writeInt(this.rank);
		this.writeString(this.actorName);
		this.writeLong(this.feats);
		this.writeByte(this.isWin);
		this.writeBytes(this.iconVO.getBytes());
		
	}
	
	
}
