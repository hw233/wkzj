package com.jtang.gameserver.module.ladder.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dataconfig.model.LadderGlobalConfig;
import com.jtang.gameserver.dataconfig.service.LadderService;
import com.jtang.gameserver.dbproxy.entity.Ladder;
import com.jtang.gameserver.module.ladder.model.LadderActorVO;

public class LadderResponse extends IoBufferSerializer {
	
	/**
	 * 玩家信息
	 */
	public LadderActorVO actorVO;

	/**
	 * 剩余战斗次数
	 */
	public int fightNum;
	
	/**
	 * 下次战斗刷新时间
	 */
	public int flushFightTime;
	
	/**
	 * 最大战斗次数
	 */
	public int maxFightNum;
	
	/**
	 * 购买战斗次数需要的点券
	 */
	public int costTicket;
	
	/**
	 * 刷新对手需要的金币
	 */
	public int costGold;
	
	/**
	 * 赛季结束时间
	 */
	public int sportEndTime;
	
	/**
	 * 对手列表
	 */
	public List<LadderActorVO> actorList = new ArrayList<>();
	
	/**
	 * 上赛季奖励
	 */
	public List<RewardObject> rewardList = new ArrayList<>();
	
	public LadderResponse(Ladder ladder,LadderActorVO actorVO, LadderGlobalConfig globalConfig,List<LadderActorVO> actorList) {
		this.actorVO = actorVO;
		this.fightNum = ladder.fightNum;
		int nextFlushTime = ladder.flushFightTime + LadderService.getGlobalConfig().flushTime;
		this.flushFightTime = nextFlushTime;
		this.maxFightNum = globalConfig.maxFightNum;
		this.costTicket = globalConfig.getCostTicket(ladder.costTicketNum);
		this.costGold = globalConfig.getCostGold(ladder.flushActor);
		this.actorList = actorList;
		this.rewardList = ladder.rewardList;
	}

	@Override
	public void write() {
		writeBytes(actorVO.getBytes());
		writeInt(fightNum);
		writeInt(flushFightTime);
		writeInt(maxFightNum);
		writeInt(costTicket);
		writeInt(costGold);
		writeInt(sportEndTime);
		writeShort((short) actorList.size());
		for(LadderActorVO vo:actorList){
			writeBytes(vo.getBytes());
		}
		writeShort((short) rewardList.size());
		for(RewardObject reward:rewardList){
			writeBytes(reward.getBytes());
		}
	}
}
