package com.jtang.gameserver.module.demon.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.FightData;
/**
 * 攻击玩家结果
 * @author ludd
 *
 */
public class DemonPlayerAttackResponse extends IoBufferSerializer {
	private FightData fightData;
	/**
	 * 奖励功勋
	 */
	private long rewardFeats;
	
	/**
	 * 攻击次数
	 */
	private int attackNum;
	
	/**
	 * 额外功勋
	 */
	private long extFeats;
	
	
	
	public DemonPlayerAttackResponse(FightData fightData, long rewardFeats, int attackNum, long extFeats) {
		super();
		this.fightData = fightData;
		this.rewardFeats = rewardFeats;
		this.attackNum = attackNum;
		this.extFeats = extFeats;
	}



	@Override
	public void write() {
		this.writeBytes(fightData.getBytes());
		this.writeLong(rewardFeats);
		this.writeInt(attackNum);
		this.writeLong(extFeats);
	}

}
