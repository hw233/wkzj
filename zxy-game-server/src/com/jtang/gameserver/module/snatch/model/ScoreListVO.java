package com.jtang.gameserver.module.snatch.model;

import static com.jtang.gameserver.module.snatch.type.SnatchEnemyType.ACTOR;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.icon.model.IconVO;

/**
 * 角色积分列表vo
 * @author 0x737263
 *
 */
public class ScoreListVO {

	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 敌人类型：{@code SnatchEnemyType}
	 */
	public int enemyType;
	
	/**
	 * 角色头像和边框
	 */
	public IconVO iconVO;
	
	/**
	 * 玩家名称
	 */
	public String actorName;
	
	/**
	 * 掌教等级
	 */
	public int level;
	
	/**
	 * 排名(计算排名)
	 */
	public int ranking;
	
	/**
	 * 当前积分
	 */
	public int score;
	
	/**
	 * 1.无抢夺来往
	 * 2.我曾经被对方抢夺(反击)
	 * 3.我曾经抢夺对方(继续抢)
	 */
	public int status = 1;
	
	/**
	 * vip等级
	 */
	public int vipLevel;
	
	/**
	 * 根据actor获取
	 * @param actor			角色
	 * @param firstHeroId	角色阵型中第一个英雄
	 * @return
	 */
	public static ScoreListVO valueOf(Actor actor, int score,int vipLevel,IconVO iconVO) {
		ScoreListVO result = new ScoreListVO();
		result.actorId = actor.getPkId();
		result.enemyType = ACTOR.getType();
		result.iconVO = iconVO;
		result.actorName = actor.actorName;
		result.level = actor.level;
		result.score = score;
		result.vipLevel = vipLevel;
		return result;
	}
	
	public void writePacket(IoBufferSerializer packet) {
		packet.writeLong(this.actorId);
		packet.writeByte((byte)this.enemyType);
		packet.writeBytes(iconVO.getBytes());
		packet.writeString(this.actorName);
		packet.writeInt(this.level);
		packet.writeInt(this.ranking);
		packet.writeInt(this.score);
		packet.writeByte((byte)this.status);
		packet.writeInt(this.vipLevel);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (actorId ^ (actorId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScoreListVO other = (ScoreListVO) obj;
		if (actorId != other.actorId)
			return false;
		return true;
	}
	
//	public int hashCode() {
//		return (String.valueOf(actorId)).hashCode();
//	}
//	
//	public boolean equals(Object vo) {
//		if (vo == null || vo instanceof ScoreListVO == false) {
//			return false;
//		}
//
//		ScoreListVO s = (ScoreListVO) vo;
//		String str = String.valueOf(s.actorId);
//		return this.actorId == s.actorId;
//		return str.equals(String.valueOf(this.actorId));
//	}
	
	
}
