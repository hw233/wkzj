package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.rhino.FormulaHelper;

/**
 * 装备强化配置:装备品质和类型决定属性值的大小
 * 
 * @author pengzy
 * 
 */
@DataFile(fileName = "equipUpgradeConfig")
public class EquipUpgradeConfig implements ModelAdapter {

	/**
	 * 装备品质,决定装备强化时增加的属性
	 */
	private int star;
	/**
	 * 装备类型
	 */
	private int type;

	/**
	 * 装备升级提升的属性值
	 */
	private int attributeValue;

	/**
	 * 强化时消耗金币的表达式
	 */
	private String goldExpr;

	

	@Override
	public void initialize() {
		//预解析公式
		Number[] args = new Number[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		FormulaHelper.execute(goldExpr, args);
	}

	public int getStar() {
		return star;
	}

	public int getAttributeValue() {
		return attributeValue;
	}
	public int getNeedGolds(int type, int level, int star) {
		return FormulaHelper.executeCeilInt(goldExpr, type, level, star);
	}
	
	public int getType() {
		return type;
	}
}
