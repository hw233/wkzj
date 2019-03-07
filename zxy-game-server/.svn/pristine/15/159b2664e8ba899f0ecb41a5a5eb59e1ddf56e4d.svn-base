package com.jtang.gameserver.module.love.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.Love;

public class LoveRankInfoResponse extends IoBufferSerializer {

	/**
	 * 自己排名
	 */
	public int rank;
	
	/**
	 * 挑战已经使用次数
	 */
	public int fightNum;
	
	/**
	 * 挑战冷却时间
	 */
	public int fightTime;
	
	/**
	 * 已经购买次数
	 */
	public int buyNum;
	
	/**
	 * 战斗力
	 */
	public int power;
	
	
	public LoveRankInfoResponse(Love love,int rank,int power){
		this.rank = rank;
		this.fightNum = love.rankFightNum;
		this.fightTime = love.rankFightTime;
		this.buyNum = love.rankFlushNum;
		this.power = power;
	}
	
	@Override
	public void write() {
		writeInt(rank);	
		writeInt(fightNum);
		writeInt(fightTime);
		writeInt(buyNum);
		writeInt(power);
	}
	
}
