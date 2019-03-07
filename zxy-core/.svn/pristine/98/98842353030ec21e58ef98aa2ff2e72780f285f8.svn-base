package com.jiatang.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

public class HeroVO extends Sprite implements Cloneable, Serializable {
	private static final long serialVersionUID = 6121779071901113893L;

	/**
	 * 仙人的配置Id
	 */
	public int heroId;
	
	/**
	 * 仙人 等级
	 */
	public int level;
	
	/**
	 * 仙人经验
	 */
	public int exp;
	
	/**
	 * 攻击力
	 */
	public int atk;
	
	/**
	 * 防御值
	 */
	public int defense;
	
	/**
	 * 生命值
	 */
	public int hp;

	
	/**
	 * 攻击距离(格子数)
	 * TODO 攻击距离读配置,不需存db
	 */
	public int atkScope;
	
	/**
	 * 技能Id
	 */
	public int skillId;
		
	/**
	 * 被动技能集合
	 */
	public List<Integer> passiveSkillList;
	
	/**
	 * 可用的潜修次数
	 */
	public int availableDelveCount;
	
	/**
	 * 已经用掉的潜修次数
	 */
	public int usedDelveCount;
	
	/**
	 * 已经突破的次数
	 */
	public int breakThroughCount;
	
	/**
	 * 上一次潜修新增的攻击值(用于重修)
	 */
	public int lastDelveAtk;
	
	/**
	 * 上一次潜修新增的防御值(用于重修)
	 */
	public int lastDelveDefense;
	
	/**
	 * 上一次潜修新增的生命值(用于重修)
	 */
	public int lastDelveHp;
	
	/**
	 * 最大血量
	 */
	private int maxHp;
	
	/**
	 * 仙人潜修累计消耗的金币
	 */
	public long delveCostGold;
	
	/**
	 * 潜修累计消耗的潜修石
	 */
	public int delveStoneNum;
	
	/**
	 * 总字段数
	 */
	private static final int COLUMN_NUM = 17;
		
	@Override
	public int getAtkScope() {
		return this.atkScope;
	}

	@Override
	public int getAtk() {
		return this.atk;
	}

	@Override
	public int getHp() {
		return this.hp;
	}

	@Override
	public int getDefense() {
		return this.defense;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public int getHeroId() {
		return this.heroId;
	}

	@Override
	public int getSkillId() {
		return this.skillId;
	}
	
	/**
	 * 是否可以重修
	 * @return  1允许  0不允许
	 */
	public int allowReDelve() {
		if (lastDelveAtk > 0 || lastDelveDefense > 0 || lastDelveHp > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public List<Integer> getPassiveSkillList() {
		return this.passiveSkillList;
	}
	
	/**
	 * HeroVO转Blob String
	 * @return
	 */
	public String parse2String() {
		List<Object> list = new ArrayList<Object>();
		list.add(this.heroId);
		list.add(this.level);
		list.add(this.exp);
		list.add(this.atk);
		list.add(this.defense);
		list.add(this.hp);
		list.add(this.atkScope);
		list.add(this.skillId);
		String passiveSkills = StringUtils.collection2SplitString(this.passiveSkillList, Splitable.BETWEEN_ITEMS);
		if (StringUtils.isNotBlank(passiveSkills)) {
			list.add(passiveSkills);
		} else {
			list.add("");
		}
		list.add(this.availableDelveCount);
		list.add(this.usedDelveCount);
		list.add(this.breakThroughCount);
		list.add(this.lastDelveAtk);
		list.add(this.lastDelveDefense);
		list.add(this.lastDelveHp);
		list.add(this.delveCostGold);
		list.add(this.delveStoneNum);

		return StringUtils.collection2SplitString(list, Splitable.ATTRIBUTE_SPLIT);
	}
	
	/**
	 * 根据blob字符串构造HeroVO
	 * @param heroArray
	 * @return
	 */
	public static HeroVO valueOf(String[] heroArray) {
		HeroVO vo = new HeroVO();
		if (heroArray.length < COLUMN_NUM) {
			ArrayList<String> arrlist = new ArrayList<>();
			for (String string : heroArray) {
				arrlist.add(string);
			}
			for (int i = heroArray.length; i < COLUMN_NUM; i++) {
				arrlist.add("0");
			}
			heroArray = arrlist.toArray(heroArray);
		}
		vo.heroId = Integer.valueOf(heroArray[0]);
		vo.level = Integer.valueOf(heroArray[1]);
		vo.exp = Integer.valueOf(heroArray[2]);
		vo.atk = Integer.valueOf(heroArray[3]);
		vo.defense = Integer.valueOf(heroArray[4]);
		vo.hp = Integer.valueOf(heroArray[5]);
		vo.atkScope = Integer.valueOf(heroArray[6]);
		vo.skillId = Integer.valueOf(heroArray[7]);

		vo.passiveSkillList = new ArrayList<Integer>();
		List<String> passSkillStrList = StringUtils.delimiterString2List(heroArray[8], Splitable.BETWEEN_ITEMS);
		for (String passSkilId : passSkillStrList) {
			vo.passiveSkillList.add(Integer.valueOf(passSkilId));
		}
		vo.availableDelveCount = Integer.valueOf(heroArray[9]);
		vo.usedDelveCount = Integer.valueOf(heroArray[10]);
		vo.breakThroughCount = Integer.valueOf(heroArray[11]);
		vo.lastDelveAtk = Integer.valueOf(heroArray[12]);
		vo.lastDelveDefense = Integer.valueOf(heroArray[13]);
		vo.lastDelveHp = Integer.valueOf(heroArray[14]);
		vo.delveCostGold = Long.valueOf(heroArray[15]);
		vo.delveStoneNum = Integer.valueOf(heroArray[16]);
		vo.maxHp = vo.hp;
		return vo;
	}
	
	/**
	 * 根据参数构造
	 * @return
	 */
	public static HeroVO valueOf(int atk, int atkScope, int defense, int heroId, int hp, int skillId, int availableDelveCount) {
		HeroVO vo = new HeroVO();
		vo.atk = atk;
		vo.atkScope = atkScope;
		vo.defense = defense;
		vo.exp = 0;
		vo.heroId = heroId;
		vo.hp = hp;
		vo.level = 1;
		vo.skillId = skillId;
		vo.passiveSkillList = new ArrayList<>();
		vo.availableDelveCount = availableDelveCount;
		vo.usedDelveCount = 0;
		vo.lastDelveAtk = 0;
		vo.lastDelveDefense = 0;
		vo.lastDelveHp = 0;
		vo.maxHp = vo.hp;
		return vo;
	}
	
//	/**
//	 * 根据参数构造
//	 * @return
//	 */
//	public static HeroVO valueOf(int atk, int atkScope, int defense, int heroId, int hp, int skillId, int level, int upgradeDelve) {
//		HeroVO vo = new HeroVO();
//		vo.atk = atk;
//		vo.atkScope = atkScope;
//		vo.defense = defense;
//		vo.exp = 0;
//		vo.heroId = heroId;
//		vo.hp = hp;
//		vo.level = level;
//		vo.skillId = skillId;
//		vo.passiveSkillList = new ArrayList<>();
//		// HeroUpgradeConfig upgradeConfig = HeroUpgradeService.get(vo.getLevel(), cfg.getStar());
//		// upgradeConfig.getUpgradeDelve();
//		vo.availableDelveCount = upgradeDelve;
//		vo.usedDelveCount = 0;
//		vo.lastDelveAtk = 0;
//		vo.lastDelveDefense = 0;
//		vo.lastDelveHp = 0;
//		vo.maxHp = vo.hp;
//		return vo;
//	}

	@Override
	public HeroVO clone() throws CloneNotSupportedException {
		HeroVO cp = (HeroVO) super.clone();
		cp.passiveSkillList = new ArrayList<>();
		cp.passiveSkillList.addAll(getPassiveSkillList());
		return cp;
	}
	
	@Override
	public void write() {
		this.writeLong(this.spriteId);
		this.writeInt(heroId);
		this.writeInt(level);
		this.writeInt(exp);
		this.writeInt(atk);
		this.writeInt(defense);
		this.writeInt(hp);
		this.writeInt(atkScope);
		this.writeInt(skillId);

		this.writeIntList(this.passiveSkillList);
		this.writeInt(availableDelveCount);
		this.writeInt(usedDelveCount);
		this.writeInt(breakThroughCount);
		this.writeInt(lastDelveAtk);
		this.writeInt(lastDelveDefense);
		this.writeInt(lastDelveHp);
		this.writeInt(this.maxHp);
		this.writeLong(this.delveCostGold);
		this.writeInt(this.delveStoneNum);
	}
	
	@Override
	public int getMaxHp() {
		return this.maxHp;
	}
	@Override
	public void setMaxHp(int value) {
		this.maxHp = value;
		
	}
	
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		this.spriteId = buffer.readLong();
		this.heroId = buffer.readInt();
		this.level = buffer.readInt();
		this.exp = buffer.readInt();
		this.atk = buffer.readInt();
		this.defense = buffer.readInt();
		this.hp = buffer.readInt();
		this.atkScope = buffer.readInt();
		this.skillId = buffer.readInt();

		this.passiveSkillList = new ArrayList<>();
		short len = buffer.readShort();
		for (int i = 0; i < len; i++) {
			int value = buffer.readInt();
			this.passiveSkillList.add(value);
		}
		this.availableDelveCount = buffer.readInt();
		this.usedDelveCount = buffer.readInt();
		this.breakThroughCount = buffer.readInt();
		this.lastDelveAtk = buffer.readInt();
		this.lastDelveDefense = buffer.readInt();
		this.lastDelveHp = buffer.readInt();
		this.maxHp = buffer.readInt();
		this.delveCostGold = buffer.readLong();
		this.delveStoneNum = buffer.readInt();
	}
	
}
