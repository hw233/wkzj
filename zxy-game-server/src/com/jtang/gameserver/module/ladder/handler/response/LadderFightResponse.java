package com.jtang.gameserver.module.ladder.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.ladder.model.LadderActorVO;

public class LadderFightResponse extends IoBufferSerializer {

	/**
	 * 战斗记录
	 */
	public FightData fightData;
	
//	/**
//	 * 胜利的场次
//	 */
//	public int winNum;
//	
//	/**
//	 * 失败的场次
//	 */
//	public int lostNum;
	
	/**
	 * 排名
	 */
	public int rank;
	
	/**
	 * 积分
	 */
	public int score;
	
	/**
	 * 掉落的物品列表
	 */
	public List<RewardObject> rewardList = new ArrayList<>();
	
	/**
	 * 新对手
	 */
	public List<LadderActorVO> actorList = new ArrayList<>();
	
	@Override
	public void write() {
		writeBytes(fightData.getBytes());
//		writeInt(winNum);
//		writeInt(lostNum);
		writeInt(rank);
		writeInt(score);
		writeShort((short) rewardList.size());
		for(RewardObject reward:rewardList){
			writeBytes(reward.getBytes());
		}
		writeShort((short) actorList.size());
		for(LadderActorVO actorVO:actorList){
			writeBytes(actorVO.getBytes());
		}
	}
}
