package com.jtang.gameserver.module.battle.model;

import java.util.List;

import com.jtang.core.utility.StringUtils;

public class Attacker {
	public byte id; // 在整场战斗中唯一
	public int heroId; // 配置id
	public short level;// 等级
	public byte x;// 坐标x
	public byte y;// 坐标y
	public int atk; // 攻击力
	public int defense; // 防御力
	public int hp; // 血量
	public int exp; // 当前等级累积的经验
	public byte atkScope; // 攻击距离
	public int hp_max;// 生命上限

	public byte type;// 类型 1=仙人 2=怪物
	public int weaponId;// 武器ID
	public short weaponLevel;// 武器等级
	public int armorId;// 防具id
	public short armorLevel;// 防具等级
	public int ornamentsId;// 饰品id
	public short ornamentsLevel;// 饰品等级
	public int skillId;// 主动技能
	public List<Integer> passiveSkills;// 已经激活的被动技能

	public byte campId;// 阵营id
	/**
	 * 已经突破的次数
	 */
	public int breakThroughCount;

	public Attacker(Fighter fighter) {
		this.id = fighter.getFighterId();
		this.heroId = fighter.getHeroId();
		this.level = (short) fighter.getLevel();
		this.x = (byte) fighter.getTile().getX();
		this.y = (byte) fighter.getTile().getY();
		this.atk = fighter.getAtk();
		this.defense = fighter.getDefense();
		this.hp = fighter.getHp();
		this.exp = 0;
		this.atkScope = (byte) fighter.getAtkScope();
		this.hp_max = fighter.getHpMax();
		this.type = (byte) fighter.type.getCode();
		this.skillId = fighter.getSkillId();
		this.passiveSkills = fighter.getPassiveSkillList();
		this.weaponId = fighter.weaponId;
		this.weaponLevel = (short) fighter.weaponLevel;
		this.ornamentsId = fighter.ornamentsId;
		this.ornamentsLevel = (short) fighter.ornamentsLevel;
		this.armorId = fighter.armorId;
		this.armorLevel = (short) fighter.armorLevel;
		this.campId = fighter.getCamp().getType();
		this.breakThroughCount = fighter.breakThroughCount;
	}

	public String format(String indentStr) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%sid=%d;", indentStr, id));
		sb.append(String.format("heroId=%d;", heroId));
		sb.append(String.format("level=%d;", level));
		sb.append(String.format("x=%d;", x));
		sb.append(String.format("y=%d;", y));
		sb.append(String.format("atk=%d;", atk));
		sb.append(String.format("defense=%d;", defense));
		sb.append(String.format("hp=%d;", hp));
		sb.append(String.format("exp=%d;", exp));
		sb.append(String.format("atkScope=%d;", atkScope));
		sb.append(String.format("hp_max=%d\r\n", hp_max));

		sb.append(String.format("type=%d\r\n", type));
		sb.append(String.format("weaponId=%d\r\n", weaponId));
		sb.append(String.format("weaponLevel=%d\r\n", weaponLevel));
		sb.append(String.format("armorId=%d\r\n", armorId));
		sb.append(String.format("armorLevel=%d\r\n", armorLevel));
		sb.append(String.format("ornamentsId=%d\r\n", ornamentsId));
		sb.append(String.format("ornamentsLevel=%d\r\n", ornamentsLevel));
		sb.append(String.format("skillId=%d\r\n", skillId));
		sb.append(String.format("passiveSkills=%s\r\n", StringUtils.collection2SplitString(passiveSkills, ";")));
		return sb.toString();
	}
}
