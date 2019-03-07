package com.jtang.gameserver.component.model;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiatang.common.model.Sprite;
import com.jtang.gameserver.dataconfig.model.AwardGoodsConfig;
import com.jtang.gameserver.dataconfig.model.MonsterConfig;


/**
 * 怪物实体
 * @author 0x737263
 *
 */
public class MonsterVO extends Sprite {
	private static final Logger LOGGER = LoggerFactory.getLogger(MonsterVO.class);
	/**
	 * 仙人配置id
	 */
	private int heroId;
	
	/**
	 * 攻击值
	 */
	private int atk;
	
	/**
	 * 防御值
	 */
	private int defense;
	
	/**
	 * 生命值
	 */
	private int hp;
	
	/**
	 * 攻击距离tile格
	 */
	private int atkScope;
	
	/**
	 * 技能
	 */
	private int skillId;
	
	/**
	 * 被动触发技能id
	 */
	private List<Integer> tiggerSkills;
	
	/**
	 * 怪物的名称
	 */
	private String name;
	
	/**
	 * 生命上限
	 */
	private int level;
	
	/**
	 * 随机掉落物品配置
	 */
	private List<AwardGoodsConfig> dropGoodsConfig;
	
	/**
	 * 怪物配置
	 */
	private MonsterConfig monsterConfig;
	
	/**
	 * 血上限
	 */
	private int maxHp;
	
	/**
	 * 是否为怪物副本
	 * 如果不是克隆出来的，是不能更改怪物数据的
	 */
	private boolean clonaed = false;
	
	public MonsterVO(MonsterConfig monsterConf) {
		this.heroId = monsterConf.getMonsterId();
		this.atk = monsterConf.getAttack();
		this.defense = monsterConf.getDefense();
		this.hp = monsterConf.getHp();
		this.atkScope = monsterConf.getAttackScope();
		this.skillId = monsterConf.getAttackSkillId();
		this.tiggerSkills = Collections.unmodifiableList(monsterConf.getPassiveSkills());
		this.name = monsterConf.getMonsterName();
		this.level = monsterConf.getLevel();
		this.dropGoodsConfig = monsterConf.getDropGoodsConfigList();
		this.monsterConfig = monsterConf;
		this.maxHp = monsterConf.getHp();
	}
	
	public MonsterVO clone(){
		MonsterVO m = new MonsterVO(monsterConfig);
		m.clonaed = true;
		return m;
	}
	
	public int getAtk() {
		return atk;
	}

	public int getDefense() {
		return defense;
	}

	public int getHp() {
		return hp;
	}

	@Override
	public int getAtkScope() {
		return atkScope;
	}

	public int getSkillId() {
		return skillId;
	}

	public List<Integer> getTiggerSkills() {
		return tiggerSkills;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int getHeroId() {
		return this.heroId;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getMaxHp() {
		return maxHp;
	}

	@Override
	public List<Integer> getPassiveSkillList() {
		return this.tiggerSkills;
	}

	public List<AwardGoodsConfig> getDropGoodsConfig() {
		return dropGoodsConfig;
	}
	
	public void setHp(int hp) {
		if (this.clonaed) {
			this.hp = hp;
		} else {
			LOGGER.error("不是克隆副本怪物，无法修改hp");
		}
	}
	
	public void setAtk(int atk) {
		if (this.clonaed) { 
			this.atk = atk;
		} else {
			LOGGER.error("不是克隆副本怪物，无法修改attack");
		}
	}
	
	public void setDefense(int defense) {
		if (this.clonaed) {
			this.defense = defense;
		} else {
			LOGGER.error("不是克隆副本怪物，无法修改defends");
		}
	}
	
	public void setMaxHp(int maxHp) {
		if (this.clonaed) {
			this.maxHp = maxHp;
		} else {
			LOGGER.error("不是克隆副本怪物，无法修改maxHp");
		}
	}
}
