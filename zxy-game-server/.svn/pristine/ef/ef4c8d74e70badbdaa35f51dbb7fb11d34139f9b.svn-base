package com.jtang.gameserver.module.demon.model;

import com.jtang.gameserver.dbproxy.entity.Demon;

public class DemonModel implements Comparable<DemonModel> {
	
	private Demon demon;
	/**
	 * 功勋值
	 */
	public long feats;
	
	/**
	 * 阵营
	 */
	private int camp;
	
	/**
	 * 难度
	 */
	public  int difficult;

	/**
	 * 攻击玩家次数
	 */
	public int attackNum;
	
	/**
	 * 攻击boss时间
	 */
	public int attackBossTime;
	
	/**
	 * 加入阵营时间
	 */
	private long joinTime;
	
	/**
	 * 攻击boss次数
	 */
	public int attackBossNum;
	
	/**
	 * 获得攻击boss额外降魔积分次数
	 */
	public int attackBossRewardScoreNum;
	
	/**
	 * 总共获得的额外积分
	 */
	public int totalRewardExtScore;
	
	
	
	public DemonModel(Demon demon, int difficult) {
		super();
		this.demon = demon;
		this.difficult = difficult;
	}

	@Override
	public int compareTo(DemonModel o) {
		if (this.feats > o.feats) {
			return -1;
		}  else if(this.feats < o.feats){
			return 1;
		} else { //功勋值相等
			if(this.joinTime < o.joinTime) {
				return -1;
			} else {
				return 1;
			}
		}
	}
	
	public Demon getDemon() {
		return demon;
	}
	
	public long getActorId() {
		return this.demon.getPkId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((demon == null) ? 0 : demon.hashCode());
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
		DemonModel other = (DemonModel) obj;
		if (demon == null) {
			if (other.demon != null)
				return false;
		} else if (!demon.equals(other.demon))
			return false;
		return true;
	}
	
	
	public void setCamp(int camp) {
		this.camp = camp;
		this.joinTime = System.currentTimeMillis();
	}
	
	public int getCamp() {
		return camp;
	}
	
	public long getJoinTime() {
		return joinTime;
	}
	


}
