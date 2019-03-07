package com.jtang.gameserver.module.demon.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.FightData;
/**
 * 打boss返回结果
 * @author ludd
 *
 */
public class DemonBossAttackResponse extends IoBufferSerializer {
	/**
	 * 战斗数据
	 */
	private FightData fightData;
	/**
	 * 奖励物品
	 */
	private List<RewardObject> rewardObjects;
	
	/**
	 * 奖励功勋值
	 */
	private long rewardFeats;
	
	/**
	 * 攻击boss倒计时(秒)
	 */
	private int time;
	
	/**
	 * 额外功勋
	 */
	private long extFeats;
	
	/**
	 * 额外获得的降魔积分
	 */
	private long extScore;
	
	
	public DemonBossAttackResponse(FightData fightData, List<RewardObject> rewardObjects, long rewardFeats, int time, long extFeats, long extScore) {
		super();
		this.fightData = fightData;
		this.rewardObjects = rewardObjects;
		this.rewardFeats = rewardFeats;
		this.time = time;
		this.extFeats = extFeats;
		this.extScore = extScore;
	}


	@Override
	public void write() {
		this.writeBytes(fightData.getBytes());
		this.writeShort((short)rewardObjects.size());
		for (RewardObject obj : rewardObjects) {
			this.writeBytes(obj.getBytes());
		}
		
		this.writeLong(rewardFeats);
		this.writeInt(time);
		this.writeLong(this.extFeats);
		this.writeLong(this.extScore);
	}

}
