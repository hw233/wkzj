package com.jtang.gameserver.module.ladder.model;

import com.jtang.core.utility.Splitable;


public class FightInfoVO {
	
	/**
	 * 发起方id
	 */
	public long actorId;
	
	/**
	 * 战斗结果
	 */
	public int fightResult;
	
	/**
	 * 战斗录像id
	 */
	public long fightVideoId;
	
	/**
	 * 战斗时间
	 */
	public int fightTime;
	
	public FightInfoVO(){
		
	}
	
	public FightInfoVO(String[] str) {
		this.actorId = Long.valueOf(str[0]);
		this.fightResult = Integer.valueOf(str[1]);
		this.fightVideoId = Long.valueOf(str[2]);
		this.fightTime = Integer.valueOf(str[3]);
	}

	public String parser2String() {
		StringBuffer sb = new StringBuffer();
		sb.append(actorId).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(fightResult).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(fightVideoId).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(fightTime);
		return sb.toString();
	}
}
