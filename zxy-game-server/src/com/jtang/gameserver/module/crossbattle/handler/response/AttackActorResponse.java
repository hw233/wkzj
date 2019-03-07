package com.jtang.gameserver.module.crossbattle.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.FightData;
/**
 * 攻击角色返回战斗结果
 * @author ludd
 *
 */
public class AttackActorResponse extends IoBufferSerializer {
	/**
	 * 战报结果
	 */
	private FightData fightData;
	
	@Override
	public void write() {
		this.writeBytes(fightData.getBytes());
	}

}
