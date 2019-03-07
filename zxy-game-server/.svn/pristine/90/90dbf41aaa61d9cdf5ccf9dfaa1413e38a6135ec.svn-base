package com.jtang.gameserver.module.power.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.PowerExt;

public class PowerInfoResponse extends IoBufferSerializer {

	/**
	 * 自己排名
	 */
	public int rank;
	
	/**
	 * 挑战冷却时间
	 */
	public int fightTime;
	
	/**
	 * 已经刷新的次数
	 */
	public int flushNum;
	
	/**
	 * 最高历史排名
	 */
	public int historyRank;
	
	
	public PowerInfoResponse(PowerExt powerExt,int rank){
		this.rank = rank;
		this.fightTime = powerExt.fightTime;
		this.flushNum = powerExt.flushNum;
		this.historyRank = powerExt.historyRank;
	}
	
	@Override
	public void write() {
		writeInt(rank);	
		writeInt(fightTime);
		writeInt(flushNum);
		writeInt(historyRank);
	}
	
}
