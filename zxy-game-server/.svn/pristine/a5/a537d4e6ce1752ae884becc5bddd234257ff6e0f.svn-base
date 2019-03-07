package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

/**
 * 怪物配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "monsterConfig")
public class MonsterConfig implements ModelAdapter {

	/**
	 * 怪物id
	 */
	private int monsterId;
	
	/**
	 * 性别
	 */
	private int sex;
	
	/**
	 * 怪物名称
	 */
	private String monsterName;
	
	/**
	 * 等级
	 */
	private int level;
	
	/**
	 * 攻击力
	 */
	private int attack;
	
	/**
	 * 攻击距离(格)
	 */
	private int attackScope;
	
	/**
	 * 防御值
	 */
	private int defense;
	
	/**
	 * 生命值
	 */
	private int hp;
	
	/**
	 * 普攻id
	 */
	private int commAtkSkillId;
	
	/**
	 * 主动技能id
	 */
	private int attackSkillId;
	
	/**
	 * 被动技能
	 */
	private String passiveSkill;
	
	/**
	 * 被击杀时掉落的物品(暂时不支持装备掉落,如果以后支持 ,也需要新开字段)
	 * 格式:
	 * <pre>
	 *  物品id_数量_概率|物品id_数量_概率
	 * </pre>
	 */
	private String dropGoods;
	
	/**
	 * 品质
	 */
	private int star;
	
	@FieldIgnore
	private transient List<Integer> passiveSkills = new ArrayList<Integer>();
	
	@FieldIgnore
	private transient List<AwardGoodsConfig> dropGoodsConfigList = new ArrayList<>();
	
	@Override
	public void initialize() {
		if (StringUtils.isNotBlank(passiveSkill)) {
			List<Integer> passiveList = StringUtils.delimiterString2List(passiveSkill, Integer.class, Splitable.ATTRIBUTE_SPLIT);
			for(Integer id : passiveList) {
				passiveSkills.add(id);
			}
		}
		
		if (StringUtils.isNotBlank(dropGoods)) {
			List<String> goods = StringUtils.delimiterString2List(dropGoods, Splitable.ELEMENT_SPLIT);
			for (String g : goods) {
				List<Integer> strList =  StringUtils.delimiterString2List(g, Integer.class, Splitable.ATTRIBUTE_SPLIT);
				int id = strList.get(0);
				int num = strList.get(1);
				int rate = strList.get(2);
				dropGoodsConfigList.add(new AwardGoodsConfig(id, num, rate));
			}
		}
		
		this.passiveSkill = null;
		this.dropGoods = null;
	}

	public int getMonsterId() {
		return monsterId;
	}

	public int getSex() {
		return sex;
	}


	public String getMonsterName() {
		return monsterName;
	}

	public int getLevel() {
		return level;
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

	public int getAttackSkillId() {
		return attackSkillId;
	}

	public String getPassiveSkill() {
		return passiveSkill;
	}

	public List<Integer> getPassiveSkills() {
		return passiveSkills;
	}

	public List<AwardGoodsConfig> getDropGoodsConfigList() {
		return dropGoodsConfigList;
	}

	public int getCommAtkSkillId() {
		return commAtkSkillId;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}
}
