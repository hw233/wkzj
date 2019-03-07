package com.jtang.gameserver.module.ally.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.FightData;
/**
 * 切磋的结果
 * @author pengzy
 *
 */
public class AllyFightResponse extends IoBufferSerializer{
	/**
	 * 奖励气势
	 */
	private int rewardMomentum;
	/**
	 * 战斗数据 
	 */
	private FightData fightData;
	
	public AllyFightResponse(FightData fightData, int rewardMomentum){
		this.fightData = fightData;
		this.rewardMomentum = rewardMomentum;
	}
	
	@Override
	public void write() {
		writeInt(rewardMomentum);
		writeBytes(fightData.getBytes());
	}

}
