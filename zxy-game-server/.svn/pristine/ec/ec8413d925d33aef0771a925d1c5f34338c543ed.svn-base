package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.module.refine.type.RefineType;

/**
 * 精炼配置
 * @author liujian
 *
 */
@DataFile(fileName = "refineEquipConfig")
public class RefineEquipConfig implements ModelAdapter{
	
	/**
	 * 装备星级
	 */
	private int equipStar;
	
	/**
	 * 装备类型
	 */
	private int equipType;
	
	/**
	 * 提升攻击最小值
	 */
	private int minAttack;
	
	/**
	 * 提升未命中攻击最大值
	 */
	public int missMaxAttack;
	
	/**
	 * 精炼效果提升攻击最大值
	 */
	private int maxAttack;
//	
//	/**
//	 * 未使用石头随机最大值的百分比
//	 */
//	private int maxAttackPercent;
	
	/**
	 * 提升防御最小值
	 */
	private int minDefense;
	
	/**
	 * 提升未命中防御最大值
	 */
	public int missMaxDefense;
	
//	/**
//	 * 未使用石头随机最大值的百分比
//	 */
//	private int maxDefensePercent;
	
	/**
	 * 提升防御最大值
	 */
	private int maxDefense;
	
	/**
	 * 提升生命最小值
	 */
	private int minHp;
	
	/**
	 * 提升未命中生命最大值
	 */
	public int missMaxHp;
	
	/**
	 * 提升生命最大值
	 */
	private int maxHp;
	
//	/**
//	 * 未使用石头随机最大值的百分比
//	 */
//	private int maxHpPercent;
	
	/**
	 * consumeStoneType1:类型1精炼石消耗个数，x1表示装备等级，x2表示已精炼次数，消耗为0表示没有此功能， 表达式=（装备等级 >1） ? 装备等级：0
	 */
	public String consumeStoneType1;
	/**
	 * consumeStoneType2:类型2精炼石消耗个数，x1表示装备等级，x2表示已精炼次数，消耗为0表示没有此功能， 表达式=（装备等级 >1） ? 装备等级：0
	 */
	public String consumeStoneType2;
	/**
	 * consumeStoneType3:类型3精炼石消耗个数，x1表示装备等级，x2表示已精炼次数，消耗为0表示没有此功能， 表达式=（装备等级 >1） ? 装备等级：0
	 */
	public String consumeStoneType3;
	
	/**
	 * 精炼消耗的金币数 表达式 =向上取整(装备等级/10)*num*(100%+此装备星级*星级百分比）；（num和星级百分比都由策划填写）
	 */
	private String consumeGold;
	
	/**
	 * 可精炼次数 表达式 =此装备等级*num；（num需要策划配6个值，对应1到6星）
	 */
	private int refineNum;
	
	@Override
	public void initialize() {
	}

	public int getEquipStar() {
		return equipStar;
	}

	public int getEquipType() {
		return equipType;
	}

	public int getRefineNum() {
		return refineNum;
	}

	public int getConsumeGold(int level, int star, int refineNum) {
		return FormulaHelper.executeCeilInt(this.consumeGold, level, star, refineNum);
	}

	public int getConsumeStone(RefineType type,int level, int refinedNum) {
		if(type == RefineType.TYPE_3){
			return FormulaHelper.executeCeilInt(consumeStoneType3, level, refinedNum);
		}else if(type == RefineType.TYPE_2){
			return FormulaHelper.executeCeilInt(consumeStoneType2, level, refinedNum);
		}else{
			return FormulaHelper.executeCeilInt(consumeStoneType1, level, refinedNum);
		}
	}

	public int getAttack(int type) {
		RefineType refineType = RefineType.getType(type);
		if(refineType.equals(RefineType.TYPE_3)){
			return this.maxAttack;
		}else{
			return RandomUtils.nextInt(this.minAttack, this.maxAttack);
		}
//		if (useGoods) {
//			return this.maxAttack;
//		}
//
//		if (RandomUtils.is100Hit(this.maxAttackPercent)) {
//			return RandomUtils.nextInt(this.minAttack, this.maxAttack);
//		}
//
//		return RandomUtils.nextInt(this.minAttack, this.missMaxAttack);
	}
	
	public int getDeffence(int type) {
		RefineType refineType = RefineType.getType(type);
		if(refineType.equals(RefineType.TYPE_3)){
			return this.maxDefense;
		}else{
			return RandomUtils.nextInt(this.minDefense, this.maxDefense);
		}
//		if (useGoods) {
//			return this.maxDefense;
//		}
//
//		if (RandomUtils.is100Hit(this.maxDefensePercent)) {
//			return RandomUtils.nextInt(this.minDefense, this.maxDefense);
//		}
//
//		return RandomUtils.nextInt(this.minDefense, this.missMaxDefense);
	}
	
	public int getHp(int type) {
		RefineType refineType = RefineType.getType(type);
		if(refineType.equals(RefineType.TYPE_3)){
			return this.maxHp;
		}else{
			return RandomUtils.nextInt(this.minHp, this.maxHp);
		}
//		if (useGoods) {
//			return this.maxHp;
//		}
//		
//		if (RandomUtils.is100Hit(this.maxHpPercent)) {
//			return RandomUtils.nextInt(this.minHp, this.maxHp);
//		}
//		
//		return RandomUtils.nextInt(this.minHp, this.missMaxHp);
	}
}
