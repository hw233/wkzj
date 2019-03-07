package com.jtang.gameserver.dataconfig.model;

import com.jiatang.common.model.EquipType;
import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.rhino.FormulaHelper;

/**
 * 装备配置
 * @author liujian
 *
 */
@DataFile(fileName = "equipConfig")
public class EquipConfig implements ModelAdapter{
	
	/**
	 * 配置id，唯一
	 */
	private int equipId;

	/**
	 * 装备名称
	 */
	private String name;
	
	/**
	 * 装备类型：1：武器 2：防具，3饰品
	 */
	private int type;

	/**
	 * 星级(1-6)
	 */
	private int star;
	
	/**
	 * 基础攻击值(装备生成时 随机)
	 */
	private int attack;
	
	/**
	 * 上限攻击值(装备生成时 随机)
	 */
	private int maxAttack;
	
	/**
	 * 基础防御值(装备生成时 随机)
	 */
	private int defense;
	
	/**
	 * 上限防御值(装备生成时 随机)
	 */
	private int maxDefense;
	
	/**
	 * 基础生命值(装备生成时 随机)
	 */
	private int hp;
	
	/**
	 * 上限生命值(装备生成时 随机)
	 */
	private int maxHp;

	/**
	 * 攻击距离(格)
	 */
	private int attackScope;

	/**
	 * 装备购买类型 1.金币 2.点券
	 */
	private int buyType;
	
	/**
	 * 装备购买价格
	 */
	private int buyPrice;
	
	/**
	 * 装备出售类型 1.金币 2.点券
	 */
	private int sellType;
	
	/**
	 * 出售价格公式
	 */
	private String sellPriceExpr;
	
	/**
	 * 需要的掌教等级
	 */
	public int needLevel;
	
	/**
	 * 装备对应的装备碎片id
	 */
	public int fragmentId;
	@Override
	public void initialize() {
		Number[] args = new Number[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		FormulaHelper.execute(this.sellPriceExpr, args);
	}

	public int getEquipId() {
		return equipId;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public int getStar() {
		return star;
	}

	public int getAttack() {
		return attack;
	}

	public int getMaxAttack() {
		return maxAttack;
	}

	public int getDefense() {
		return defense;
	}

	public int getMaxDefense() {
		return maxDefense;
	}

	public int getHp() {
		return hp;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public int getAttackScope() {
		return attackScope;
	}

	public int getBuyType() {
		return buyType;
	}

	public int getBuyPrice() {
		return buyPrice;
	}

	public int getSellType() {
		return sellType;
	}

	public int getSellPrice(int level, int refineNum) {
		return FormulaHelper.executeCeilInt(this.sellPriceExpr, level, refineNum);
	}
	
	/**
	 * 获取装备类型枚举
	 * @return
	 */
	public EquipType getEquipType() {
		return EquipType.getType(this.type);
	}
	
	/**
	 * 是否为武器
	 * @return
	 */
	public boolean isWeapon(){
		return type == EquipType.WEAPON.getId();
	}
	
	/**
	 * 是否为防具
	 * @return
	 */
	public boolean isArmor(){
		return type == EquipType.ARMOR.getId();
	}
	
	/**
	 * 是否为饰品
	 * @return
	 */
	public boolean isOrnaments(){
		return type == EquipType.ORNAMENTS.getId();
	}
}
