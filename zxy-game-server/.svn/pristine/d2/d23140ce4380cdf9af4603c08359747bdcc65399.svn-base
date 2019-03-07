package com.jtang.gameserver.module.trialcave.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.trialcave.model.TrialCaveInfoVO;

/**
 * 试炼战斗结果
 * @author lig
 *
 */
public class TrialBattleResultResponse extends IoBufferSerializer {
	
	/**
	 * 战斗数据
	 */
	public FightData fightData;
	
	/**
	 * 已经奖励的物品
	 * 格式:List<RewardObject>
	 */
	public List<RewardObject> rewards;
	
	/**
	 * 关卡战斗信息更新
	 */
	public TrialCaveInfoVO entranceVO;
	
	/**
	 * 
	 * @param fightData
	 * @param rewards
	 * @param rewards
	 */
	public TrialBattleResultResponse(FightData fightData, List<RewardObject> rewards, TrialCaveInfoVO entranceVO) {
		this.fightData = fightData;
		this.rewards = rewards;
		this.entranceVO = entranceVO;
	}
	
	@Override
	public void write() {
		this.writeBytes(fightData.getBytes());
		this.writeShort((short)rewards.size());
		for (RewardObject obj : rewards) {
			this.writeBytes(obj.getBytes());
		}
		this.writeBytes(entranceVO.getBytes());
	}
}
