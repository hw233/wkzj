package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

/**
 * 仙人配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "heroConfig")
public class HeroConfig implements ModelAdapter {

	@FieldIgnore
	private static final Logger LOGGER = LoggerFactory.getLogger(HeroConfig.class);
	/**
	 * 仙人Id
	 */
	private int heroId;
	
	/**
	 * 性别  0.女  1.男
	 */
	private int sex;
	
	/**
	 * 仙人名
	 */
	private String heroName;
	
	/**
	 * 基础攻击力
	 */
	private int attack;
	
	/**
	 * 基础攻击距离
	 */
	private int attackScope;
	
	/**
	 * 基础防御值
	 */
	private int defense;
	
	/**
	 * 基础生命值
	 */
	private int hp;
	
	/**
	 * 普攻id
	 */
	private int commAtkSkillId;
	
	/**
	 * 主动攻击技能列表
	 */
	private int attackSkillId;
	
	/**
	 * 被动技能列表
	 */
	private String passiveSkill;
	
	/**
	 * 星级
	 */
	private int star;
	
	/**
	 * 招募该仙人需要消耗的魂魄数
	 */
	private int recruitSoulCount;
	
	/**
	 * 升级时攻击值系数
	 */
	private float upgradeAttack;
	
	/**
	 * 升级时防御系数
	 */
	private float upgradeDefense;
	
	/**
	 * 升级时生命系数
	 */
	private float upgradeHp;
	
	/**
	 * 主力仙人随机千分比: 属性id(生命1,攻击4,防御5)_千分比   1_250|4_250|5_500
	 */
	private String mainHeroRate;
	
	
	@FieldIgnore
	private transient List<Integer> passiveSkills = new ArrayList<Integer>();
	
	@FieldIgnore
	private Map<Integer, Integer> mainHeroRateMap = new HashMap<Integer, Integer>();

	@Override
	public void initialize() {	
		if (StringUtils.isNotBlank(passiveSkill)) {
			List<Integer> passiveList = StringUtils.delimiterString2List(passiveSkill, Integer.class, Splitable.ATTRIBUTE_SPLIT);
			for(int id : passiveList) {
				passiveSkills.add(id);
			}
			this.passiveSkill = null;
		}
		
		if (StringUtils.isNotBlank(this.mainHeroRate)){
			mainHeroRateMap = StringUtils.delimiterString2IntMap(this.mainHeroRate);
			this.mainHeroRate = null;
		}
		
	}

	public int getHeroId() {
		return heroId;
	}
	
	public int getSex() {
		return sex;
	}

	public String getHeroName() {
		return heroName;
	}

	public int getAttack() {
		return attack;
	}

	public int getAttackScope() {
		return attackScope;
	}

	public int getDefense() {
		return defense;
	}

	public int getHp() {
		return hp;
	}
	
	public int getCommAtkSkillId() {
		return commAtkSkillId;
	}

	public int getAttackSkillId() {
		return attackSkillId;
	}

	public List<Integer> getPassiveSkills() {
		return passiveSkills;
	}

	public int getStar() {
		return star;
	}

	public int getRecruitSoulCount() {
		return recruitSoulCount;
	}
	
	public float getUpgradeAttack() {
		return upgradeAttack;
	}

	public float getUpgradeDefense() {
		return upgradeDefense;
	}
	
	public float getUpgradeHp() {
		return upgradeHp;
	}
	/**
	 * 获取随机主力仙人属性
	 * @return
	 */
	public int getRaomdomAttributeKey() {
		Integer id = RandomUtils.randomHit(1000, this.mainHeroRateMap);
		if (id != null) {
			return id;
		}
		LOGGER.error("无法随即到一个属性id");
		return 1;
	}
	
	@Override
	public String toString() {
		return "heroId:[" + heroId + "] sex:[" + sex + "] heroName:[" + heroName + "] attack:[" + attack + "] attackScope:[" + attackScope
				+ "] defense:[" + defense + "] hp:[" + hp + "] attackSkill:[" + attackSkillId + "] passiveSkill:[" + passiveSkill + "]";
	}
}
