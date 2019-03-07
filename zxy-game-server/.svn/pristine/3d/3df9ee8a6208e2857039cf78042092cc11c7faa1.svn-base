package com.jtang.gameserver.module.love.model;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

public class FightVO{

	/**
	 * 挑战者角色
	 */
	public long actorId;
	
	/**
	 * 战斗结果
	 */
	public int fightResult;
	
	/**
	 * 战斗动画id
	 */
	public long fightVideo;
	
	/**
	 * 挑战时间
	 */
	public int fightTime;
	
	public FightVO(){
		
	}
	
	public FightVO(long actorId,int fightResult,long fightVideo,int fightTime){
		this.actorId = actorId;
		this.fightResult = fightResult;
		this.fightVideo = fightVideo;
		this.fightTime = fightTime;
	}
	
	
	public static FightVO valueOf(String str[]){
		FightVO fightVO = new FightVO();
		str = StringUtils.fillStringArray(str, 4, "0");
		fightVO.actorId = Long.valueOf(str[0]);
		fightVO.fightResult = Integer.valueOf(str[1]);
		fightVO.fightVideo = Long.valueOf(str[2]);
		fightVO.fightTime = Integer.valueOf(str[3]);
		return fightVO;
	}
	
	public String parser2String(){
		StringBuffer sb = new StringBuffer();
		sb.append(actorId).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(fightResult).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(fightVideo).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(fightTime);
		return sb.toString();
		
	}
}
