package com.jtang.gameserver.module.snatch.model;

import static com.jtang.gameserver.module.snatch.type.SnatchEnemyType.ACTOR;

import java.util.concurrent.atomic.AtomicLong;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dataconfig.service.IconService;
import com.jtang.gameserver.dataconfig.service.RandomNameService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.module.snatch.type.SnatchEnemyType;

/**
 * 抢夺敌人vo
 * @author 0x737263
 *
 */
public class SnatchEnemyVO {
	
	/**
	 * 机器人角色id自增类(从1开始)
	 */
	private static AtomicLong robotActorId = new AtomicLong(1);

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 敌人类型：{@code SnatchEnemyType}
	 */
	public int enemyType;
	
	/**
	 * 英雄配置id
	 */
	public IconVO iconVO;
	
	/**
	 * 玩家名称
	 */
	public String actorName;
	
	/**
	 * 掌教等级
	 */
	public int actorLevel;
	
	/**
	 * 金币
	 */
	public long gold;
	
	/**
	 * 角色vip等级
	 */
	public int vipLevel;
	
	/**
	 * 是否允许抢夺金币 .  0.允许  1.不允许
	 */
	public int allowSnatchGold;
	
	/**
	 * 扣金币
	 * @param gold
	 */
	public void decreaseGold(int gold) {
		if (this.gold < gold) {
			this.gold = 0;
		} else {
			this.gold -= gold;
		}
	}
	
	/**
	 * 机器人加金币
	 * @param gold
	 */
	public void increase(int gold) {
		this.gold += gold;
	}
	
	/**
	 * 构造机器人vo
	 * @param heroId
	 * @param actorLevel
	 * @param gold
	 * @return
	 */
	public static SnatchEnemyVO valueOfRobot(int actorLevel, int gold) {
		SnatchEnemyVO result = new SnatchEnemyVO();
		result.actorId = robotActorId.getAndIncrement();
		result.actorName = RandomNameService.randActorName();
		result.enemyType = SnatchEnemyType.ROBOT.getType();
		result.iconVO = IconService.randomIconVO();
		result.actorLevel = actorLevel;
		result.gold = gold;
		return result;
	}
	
	/**
	 * 构造真实角色vo
	 * @param actor
	 * @return
	 */
	public static SnatchEnemyVO valueOfActor(Actor actor, int vipLevel, boolean allowSnatchGold,IconVO iconVO) {
		SnatchEnemyVO result = new SnatchEnemyVO();
		result.actorId = actor.getPkId();
		result.actorName = actor.actorName;
		result.enemyType = ACTOR.getType();
		result.iconVO = iconVO;
		result.actorLevel = actor.level;
		result.gold = actor.gold;
		result.vipLevel = vipLevel;
		result.allowSnatchGold = allowSnatchGold ? 0 : 1;
		return result;
	}
	
	public void writePacket(IoBufferSerializer packet) {
		packet.writeLong(this.actorId);
		packet.writeByte((byte) this.enemyType);
		packet.writeBytes(this.iconVO.getBytes());
		packet.writeString(this.actorName);
		packet.writeInt(this.actorLevel);
		packet.writeLong(this.gold);
		packet.writeInt(this.vipLevel);
		packet.writeByte((byte) this.allowSnatchGold);
	}

}
