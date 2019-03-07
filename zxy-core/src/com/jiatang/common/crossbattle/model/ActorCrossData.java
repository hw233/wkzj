package com.jiatang.common.crossbattle.model;

import java.util.Map;
import java.util.Map.Entry;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.TimeUtils;

public class ActorCrossData extends IoBufferSerializer {

	
	/**
	 * 本服排名
	 */
	public int powerRank;
	
	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 角色名
	 */
	public String actorName;
	
	/**
	 * 角色等级
	 */
	public int level;

	/**
	 * 当前血量
	 */
	public int hp;
	
	/**
	 * 阵形装备数据
	 */
	public byte[] lineupEquipData;
	
	/**
	 * 气势
	 */
	public int morale;
	
	/**
	 * 最大值(报名时计算好)
	 */
	public int hpMax;
	
	/**
	 * vip等级
	 */
	public int vipLevel;
	
	/**
	 * 阵形数据
	 */
	private LineupFightModel fightModel;
	
	/**
	 * 装备数据
	 */
	private ViewLineupVO viewLineupVO;
	
	/**
	 * 杀人数
	 */
	private int killNum;
	
	/**
	 *  获得伤害量
	 */
	private long rewardHurt;
	
	/**
	 * 主动攻击次数
	 */
	private int attackNum;
	
	/**
	 * 死亡时间
	 */
	private int deadTime;
	
	/**
	 * 连杀次数
	 */
	private int continueKillNum;
	
	/**
	 * 被杀次数
	 */
	private int beKilledNum;
	
	
	
	/**
	 * 攻击时间
	 */
	private int attackTime;
	
	/**
	 * 复活时间
	 */
	private int reviveTime;
	
	//------------------------------
	
	/**
	 * 伤害排名
	 */
	public int hurtRank;
	
	/**
	 * 最高连杀
	 */
	private int maxContuneKillNum;
	
	
	
	public ActorCrossData() {
	}
	
	public ActorCrossData(int rank, long actorId, String actorName, int level, byte[] lineupFightModel, int hp, byte[] lineupEquipData, int morale,
			int currentHp, int vipLevel) {
		super();
		this.powerRank = rank;
		this.actorId = actorId;
		this.actorName = actorName;
		this.level = level;
		this.hp = hp;
		this.lineupEquipData = lineupEquipData;
		this.morale = morale;
		this.hpMax = this.hp;
		this.vipLevel = vipLevel;
		fightModel = new LineupFightModel(lineupFightModel);
		viewLineupVO = new ViewLineupVO(this.lineupEquipData);
	}




	@Override
	public void write() {
		this.writeInt(this.powerRank);
		this.writeLong(this.actorId);
		this.writeString(this.actorName);
		this.writeInt(this.level);
		this.writeByteAarry(this.fightModel.getBytes());
		this.writeInt(this.hp);
		this.writeByteAarry(this.lineupEquipData);
		this.writeInt(this.morale);
		this.writeInt(this.hpMax);
		this.writeInt(this.vipLevel);
		this.writeInt(this.deadTime);
		this.writeInt(this.continueKillNum);
		this.writeInt(this.killNum);
		this.writeLong(this.rewardHurt);
		this.writeInt(this.attackTime);
		this.writeInt(this.reviveTime);
	}
	
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		this.powerRank = buffer.readInt();
		this.actorId = buffer.readLong();
		this.actorName = buffer.readString();
		this.level = buffer.readInt();
		byte[] lineupFightModel = buffer.readByteArray();
		this.hp = buffer.readInt();
		this.lineupEquipData = buffer.readByteArray();
		this.morale = buffer.readInt();
		this.hpMax = buffer.readInt();
		this.vipLevel = buffer.readInt();
		this.deadTime = buffer.readInt();
		this.continueKillNum = buffer.readInt();
		this.killNum = buffer.readInt();
		this.rewardHurt = buffer.readLong();
		this.attackTime = buffer.readInt();
		this.reviveTime = buffer.readInt();
		
		fightModel = new LineupFightModel(lineupFightModel);
		viewLineupVO = new ViewLineupVO(this.lineupEquipData);
	}

	public boolean isDead() {
		if (hp <=0) {
			return true;
		}
		return false;
	}

	
	/**
	 *  记录玩家受到伤害和产生的伤害
	 * @param heroHurtMaps 收到的伤害
	 * @param rewardHurts 获得的伤害
	 * @param homeActor 是否是主动方
	 */
	public void updateHero(Map<Integer, Integer> heroHurtMaps, Map<Integer, Integer> rewardHurts) {
		Map<Integer, HeroVO> heros = fightModel.getHeros();
		for (Entry<Integer, Integer> entry : heroHurtMaps.entrySet()) {
			int heroId = entry.getKey();
			int hurt = heroHurtMaps.get(entry.getKey());
			for (HeroVO heroVO : heros.values()) {
				if (heroId == heroVO.heroId){
					Map<AttackerAttributeKey, Integer> att = fightModel.getAttributeChanges().get(heroVO.getSpriteId());
					int heroHp = heroVO.hp;
					if (att.containsKey(AttackerAttributeKey.HP)) {
						heroHp += att.get(AttackerAttributeKey.HP);
					}
					if (att.containsKey(AttackerAttributeKey.HP_MAX)) {
						heroHp += att.get(AttackerAttributeKey.HP_MAX);
					}
					int heroHurt = heroHp >= hurt ? hurt : heroHp;
					if (heroHp > 0) {
						heroVO.hp -= heroHurt;
					}
				}
			}
			hp -= hurt;
		}
		
		
		
		if (hp < 0 || allHeroDead()) {
			hp = 0;
		}
		
		if (isDead()) {
			setDeadTime(TimeUtils.getNow());
			continueKillNum = 0;
			beKilledNum += 1;
		}
	}
	
	private boolean allHeroDead() {
		Map<Integer, HeroVO> heros = fightModel.getHeros();
		for (HeroVO heroVO : heros.values()) {
			Map<AttackerAttributeKey, Integer> att = fightModel
					.getAttributeChanges().get(heroVO.getSpriteId());
			int heroHp = heroVO.hp;
			if (att.containsKey(AttackerAttributeKey.HP)) {
				heroHp += att.get(AttackerAttributeKey.HP);
			}
			if (att.containsKey(AttackerAttributeKey.HP_MAX)) {
				heroHp += att.get(AttackerAttributeKey.HP_MAX);
			}
			if (heroHp > 0) {
				return false;
			}
		}
		return true;

	}
	
	
	public LineupFightModel getFightModel() {
		return fightModel;
	}
	
	public ViewLineupVO getViewLineupVO() {
		return viewLineupVO;
	}

	public void updateKillNum() {
		killNum += 1;
		continueKillNum += 1;
		maxContuneKillNum = maxContuneKillNum > continueKillNum ? maxContuneKillNum : continueKillNum;
	}
	
	public int getKillNum() {
		return killNum;
	}

	
	public int getTotalAttribute(AttackerAttributeKey key) {
		if (key.equals(AttackerAttributeKey.ATK)) {
			int atk = 0;
			Map<Integer, HeroVO> heros = fightModel.getHeros();
			Map<Long, Map<AttackerAttributeKey, Integer>> att = fightModel.getAttributeChanges();
			for (HeroVO heroVO : heros.values()) {
				atk += heroVO.getAtk();
				if (att.containsKey(heroVO.getSpriteId())) {
					Map<AttackerAttributeKey, Integer> map = att.get(heroVO.getSpriteId());
					if (map.containsKey(AttackerAttributeKey.ATK)) {
						atk += map.get(AttackerAttributeKey.ATK);
					}
				}
			}
			return atk;
		} else if (key.equals(AttackerAttributeKey.DEFENSE)) {
			int def = 0;
			Map<Integer, HeroVO> heros = fightModel.getHeros();
			Map<Long, Map<AttackerAttributeKey, Integer>> att = fightModel.getAttributeChanges();
			for (HeroVO heroVO : heros.values()) {
				def += heroVO.getDefense();
				if (att.containsKey(heroVO.getSpriteId())) {
					Map<AttackerAttributeKey, Integer> map = att.get(heroVO.getSpriteId());
					if (map.containsKey(AttackerAttributeKey.DEFENSE)) {
						def += map.get(AttackerAttributeKey.DEFENSE);
					}
				}
			}
			return def;
		} else if (key.equals(AttackerAttributeKey.HP_MAX)) {
			 int hpMax = 0;
			Map<Integer, HeroVO> heros = fightModel.getHeros();
			Map<Long, Map<AttackerAttributeKey, Integer>> att = fightModel.getAttributeChanges();
			for (HeroVO heroVO : heros.values()) {
				hpMax += heroVO.getMaxHp();
				if (att.containsKey(heroVO.getSpriteId())) {
					Map<AttackerAttributeKey, Integer> map = att.get(heroVO.getSpriteId());
					 if (map.containsKey(AttackerAttributeKey.HP_MAX)) {
						 hpMax += map.get(AttackerAttributeKey.HP_MAX);
					 }
				}
			}
			return hpMax;
		}
		return 0;
	}
	
	public void addRewardHurt(int value) {
		rewardHurt += value;
	}
	public long getRewardHurt() {
		return rewardHurt;
	}
	
	public void addAttackNum() {
		attackNum += 1;
	}
	
	public int getAttackNum() {
		return attackNum;
	}
	
	public int getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(int deadTime) {
		this.deadTime = deadTime;
	}
	public void revive() {
		this.hp = this.hpMax;
		Map<Integer, HeroVO> heros = fightModel.getHeros();
		for (HeroVO heroVO : heros.values()) {
			heroVO.hp = heroVO.getMaxHp();
		}
		setDeadTime(0);
		int reviveTime = TimeUtils.getNow();
		setReviveTime(reviveTime);
	}
	
	public int getContinueKillNum() {
		return continueKillNum;
	}

	public int getBeKilledNum() {
		return beKilledNum;
	}
	
	public int getMaxContuneKillNum() {
		return maxContuneKillNum;
	}
	
	public void setReviveTime(int reviveTime) {
		this.reviveTime = reviveTime;
	}
	
	public void setAttackTime(int attackTime) {
		this.attackTime = attackTime;
	}
	
	public int getAttackTime() {
		return attackTime;
	}
	
	public int getReviveTime() {
		return reviveTime;
	}
	
}
