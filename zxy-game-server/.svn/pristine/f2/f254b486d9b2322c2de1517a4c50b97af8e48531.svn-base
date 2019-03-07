package com.jtang.gameserver.module.extapp.beast.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.FightData;

/**
 * 战斗结果
 * @author ligang
 *
 */
public class BeastAttackResponse extends IoBufferSerializer {
	/**
	 * 增加的张角经验
	 */
	public long exp;
	
	/*8
	 * 战斗数据
	 */
	public FightData fightData;
	/**
	 * 奖励列表
	 */
	public List<RewardObject> resultList = new ArrayList<RewardObject>();
	
	/**
	 *冷却时间
	 */
	public int CDTime;
	
	public BeastAttackResponse(long exp, FightData fightData, List<RewardObject> resultList, int cdTime){
		this.exp = exp;
		this.fightData = fightData;
		this.resultList = resultList;
		this.CDTime = cdTime;
	}
	
	@Override
	public void write() {
		writeLong(exp);
		writeBytes(fightData.getBytes());
		writeShort((short)resultList.size());
		for (RewardObject rewardObject : resultList) {
			writeBytes(rewardObject.getBytes());
		}
		writeInt(this.CDTime);
	}
	
}
