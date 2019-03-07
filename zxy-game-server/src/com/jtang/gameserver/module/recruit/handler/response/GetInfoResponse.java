package com.jtang.gameserver.module.recruit.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 获取聚仙阵信息
 * @author 0x737263
 *
 */
public class GetInfoResponse extends IoBufferSerializer  {
	
	/**
	 *  小聚仙总冷却时间
	 */
	private int smallTotalTime;
	/**
	 *  大聚仙总冷却时间
	 */
	private int bigTotalTime;
	
	/**
	 * 到达保底小聚仙次数
	 */
	private int smallLeastNum;
	/**
	 * 到达保底小聚仙次数
	 */
	private int bigLeastNum;
	

	


	public GetInfoResponse( int smallTotalTime, int bigTotalTime, int smallLeastNum, int bigLeastNum) {
		super();
		this.smallTotalTime = smallTotalTime > 0 ? smallTotalTime : 0;
		this.bigTotalTime = bigTotalTime > 0 ? bigTotalTime : 0;
		this.smallLeastNum = smallLeastNum;
		this.bigLeastNum = bigLeastNum;
	}





	@Override
	public void write() {
		this.writeInt(this.smallTotalTime);
		this.writeInt(this.bigTotalTime);
		this.writeInt(this.smallLeastNum);
		this.writeInt(this.bigLeastNum);
	}
}
