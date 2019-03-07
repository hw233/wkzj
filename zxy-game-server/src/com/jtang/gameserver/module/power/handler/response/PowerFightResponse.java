package com.jtang.gameserver.module.power.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.FightData;

public class PowerFightResponse extends IoBufferSerializer {
	
	/**
	 * 挑战类型(1.向上挑战 2.向下挑战)
	 */
	public int fightType;
	
	/**
	 * 挑战的排名
	 */
	public int targetRank;
	
	/**
	 * 被挑战者的id
	 */
	public long targetId;
	
	/**
	 * 表示抢占名次是否成功：战斗胜利了，但不一定抢占名次成功
	 * 只有战斗胜利的时候，此值才有意义
	 * 抢占名次是否成功，1为成功，0为失败
	 * 此值为1时的提示：“挑战成功！你击败了[玩家名]，现在你成为最强势力第x名！”
	 * 此值为0时的提示："挑战成功！但已经有人比你先击败了此人，得到了其名次"
	 */
	public int captureSuccess;
	
	/**
	 * 战斗数据
	 */
	public FightData data;
	
	/**
	 * 新历史排名
	 */
	public int historyRank;
	
	/**
	 * 被挑战方损失气势值
	 */
	public int costMorale;
	
	/**
	 * 获得气势值
	 */
	public int addMorale;
	
	/**
	 * 新晋排名奖励
	 */
	public List<RewardObject> list = new ArrayList<>();
	
	/**
	 * 战斗时间
	 */
	public int fightTime;
	
	@Override
	public void write() {
		writeByte((byte)fightType);
		writeInt(targetRank);
		writeLong(targetId);
		writeByte((byte)captureSuccess);
		writeBytes(data.getBytes());
		writeInt(historyRank);
		writeInt(costMorale);
		writeInt(addMorale);
		writeShort((short) list.size());
		for(RewardObject rewardObject:list){
			writeBytes(rewardObject.getBytes());
		}
		writeInt(fightTime);
	}
}
