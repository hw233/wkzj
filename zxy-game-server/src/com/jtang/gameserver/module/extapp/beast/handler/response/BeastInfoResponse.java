package com.jtang.gameserver.module.extapp.beast.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dataconfig.service.BeastService;

/**
 * 年兽信息
 * @author ligang
 *
 */
public class BeastInfoResponse extends IoBufferSerializer {
	
	/**
	 * 活动开始时间
	 */
	public int startTime;
	
	/**
	 * 活动结束时间
	 */
	public int endTime;
	/**
	 * 攻击次数
	 */
	public int actCount;
	
	/**
	 * 倒计时
	 */
	public int countdown;
	
	/**
	 *冷却时间
	 */
	public int CDTime;
	
	public BeastInfoResponse(int actCount,int countdown,int CDTime){
		this.actCount = actCount;
		this.countdown = countdown;
		this.CDTime = CDTime;
		this.startTime = BeastService.startTime;
		this.endTime = BeastService.endTime;
	}
	
	@Override
	public void write() {
		writeInt(this.startTime);
		writeInt(this.endTime);
		writeInt(this.actCount);
		writeInt(this.countdown);
		writeInt(this.CDTime);
	}
	
}
