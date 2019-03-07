package com.jtang.gameserver.module.recruit.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class RandRecruitRequest extends IoBufferSerializer {
	/**
	 * 聚仙类型
	 * 1：小聚仙
	 * 2：大聚仙
	 */
	public byte recruitType;
	
	/**
	 * 抽取类型
	 * 1：单次
	 * 2：10次
	 */
	public byte single;

	public RandRecruitRequest(byte[] bytes) {
		super(bytes);
	}
	
	
	@Override
	protected void read() {
		this.recruitType = readByte();
		this.single = readByte();
	}
	
	
}
