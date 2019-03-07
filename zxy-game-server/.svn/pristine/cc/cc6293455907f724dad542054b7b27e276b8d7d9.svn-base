package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.module.delve.type.DelveType;

/**
 * 仙人潜修配置
 * 
 * @author ludd
 * 
 */
@DataFile(fileName = "heroDelveConfig")
public class HeroDelveConfig implements ModelAdapter {

	/**
	 * 仙人ID
	 */
	private int heroId;
	/**
	 * 提升攻击力最小值
	 */
	private int attackMin;
	
	/**
	 * 提升攻击力未命中时最大值
	 */
	private int attackMissMax;

	/**
	 * 提升攻击力最大值
	 */
	private int attackMax;
	
//	/**
//	 * 随机攻击最大值的百分比
//	 */
//	private int attackMaxPercent;

	/**
	 * 提升防御力最小值
	 */
	private int defeneseMin;
	
	/**
	 * 提升防御未命中最大值
	 */
	private int defenseMissMax;

	/**
	 * 提升防御力最大值
	 */
	private int defeneseMax;
	
//	/**
//	 * 随机防御最大值的百分比
//	 */
//	private int defenseMaxPercent;

	/**
	 * 提升生命最小值
	 */
	private int hpMin;
	
	/**
	 * 提升生命未命中最大值 
	 */
	private int hpMissMax;

	/**
	 * 提升生命最大值
	 */
	private int hpMax;
	
//	/**
//	 * 随机生命最大值的百分比
//	 */
//	private int hpMaxPercent;
	
	/**
	 * 类型1消耗潜修石数量，表达式=英雄等级*系数（系数由策划配置）
	 */
	private String consumeStoneType1;
	
	/**
	 * 类型2消耗潜修石数量，表达式=英雄等级*系数（系数由策划配置）
	 */
	private String consumeStoneType2;
	
	/**
	 * 类型3消耗潜修石数量，表达式=英雄等级*系数（系数由策划配置）
	 */
	private String consumeStoneType3;
	
	/**
	 * 消耗金币，表达式=英雄品质*英雄潜修次数*基础值（基础值由策划配置）
	 */
	private String consumeGold;

	@Override
	public void initialize() {
	}
	
	/**
	 * 需要消耗石头个数
	 * @param heroLevel
	 * @param usedDelveCount
	 * @return
	 */
	public int getConsumeStone(DelveType type,int heroLevel, int usedDelveCount){
		if(type == DelveType.TYPE_3){
			return FormulaHelper.executeCeilInt(consumeStoneType3, heroLevel, usedDelveCount);
		}else if(type == DelveType.TYPE_2){
			return FormulaHelper.executeCeilInt(consumeStoneType2, heroLevel, usedDelveCount);
		}else{
			return FormulaHelper.executeCeilInt(consumeStoneType1, heroLevel, usedDelveCount);
		}
	}
	
	/**
	 * 需要金币数
	 * @param star				星级
	 * @param usedDelveCount	已潜修次数
	 * @return
	 */
	public int getConsumeGold(int star, int usedDelveCount) {
		return FormulaHelper.executeCeilInt(consumeGold, star, usedDelveCount);
	}

	public int getHeroId() {
		return heroId;
	}

	public int getAttackMax() {
		return attackMax;
	}

	public int getDefeneseMax() {
		return defeneseMax;
	}
	
	public int getHpMax() {
		return hpMax;
	}

	/**
	 * 随机攻击值
	 * @param useGoods		是否使用石头
	 * @param proportion	属性增加百分比
	 * @return
	 */
	public int getRandomAttack(int type, float proportion) {
		int minValue = getMathValue(this.attackMin, proportion);
		int missValue = getMathValue(this.attackMissMax, proportion);
		int maxValue = getMathValue(this.attackMax, proportion);
		
//		if(useGoods) {
//			return maxValue;
//		}
//
//		if (RandomUtils.is100Hit(this.attackMaxPercent)) {
//			return RandomUtils.nextInt(missValue, maxValue);
//		}
//
//		return RandomUtils.nextInt(minValue, missValue);
		DelveType delveType = DelveType.getType(type);
		if(delveType.equals(DelveType.TYPE_3)){
			return maxValue;
		}else if(delveType.equals(DelveType.TYPE_2)){
			return RandomUtils.nextInt(missValue, maxValue);
		}else{
			return RandomUtils.nextInt(minValue, maxValue);
		}
	}

	/**
	 * 未使用石头随机生命值
	 * @param proportion
	 * @return
	 */
	public int getRandomHp(int type, float proportion) {
		int minValue = getMathValue(this.hpMin,proportion); 
		int missValue = getMathValue(this.hpMissMax,proportion);  
		int maxValue = getMathValue(this.hpMax,proportion);  
		
//		if(useGoods) {
//			return maxValue;
//		}
//		
//		if(RandomUtils.is100Hit(this.hpMaxPercent)) {
//			return RandomUtils.nextInt(missValue, maxValue);
//		}
//		
//		return RandomUtils.nextInt(minValue, missValue);
		DelveType delveType = DelveType.getType(type);
		if(delveType.equals(DelveType.TYPE_3)){
			return maxValue;
		}else if(delveType.equals(DelveType.TYPE_2)){
			return RandomUtils.nextInt(missValue, maxValue);
		}else{
			return RandomUtils.nextInt(minValue, maxValue);
		}
	}
	
	/**
	 * 未使用石头随机防御值
	 * @param proportion
	 * @return
	 */
	public int getRandomDefence(int type, float proportion) {
		int minValue = getMathValue(this.defeneseMin,proportion); 
		int missValue = getMathValue(this.defenseMissMax,proportion);
		int maxValue = getMathValue(this.defeneseMax,proportion);
		
//		if(useGoods) {
//			return maxValue;
//		}
//
//		if (RandomUtils.is100Hit(this.defenseMaxPercent)) {
//			return RandomUtils.nextInt(missValue, missValue);
//		}
//
//		return RandomUtils.nextInt(minValue, maxValue);
		DelveType delveType = DelveType.getType(type);
		if(delveType.equals(DelveType.TYPE_3)){
			return maxValue;
		}else if(delveType.equals(DelveType.TYPE_2)){
			return RandomUtils.nextInt(missValue, maxValue);
		}else{
			return RandomUtils.nextInt(minValue, maxValue);
		}
	}
	
	private int getMathValue(int value, float proportion) {
		return Math.round(value * (1 + proportion));
	}
}
