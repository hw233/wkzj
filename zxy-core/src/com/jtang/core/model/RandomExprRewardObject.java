package com.jtang.core.model;

import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.StringUtils;
/**
 * 随机奖励物品（个数由表达式计算)
 * @author ludd
 *
 */
public class RandomExprRewardObject extends RandomRewardObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8291894757298515111L;
	/**
	 * 个数表达式
	 */
	private String numExpression;
	public static RandomExprRewardObject valueOf(String[] record) {
		record = StringUtils.fillStringArray(record, 4, "0");
		RandomExprRewardObject rewardObject = new RandomExprRewardObject();
		rewardObject.rewardType = RewardType.getType(Integer.valueOf(record[0]));
		rewardObject.id = Integer.valueOf(record[1]);
		rewardObject.numExpression = record[2];
		rewardObject.rate = Integer.valueOf(record[3]);
		return rewardObject;
	}
	/**
	 * 计算数量
	 * @param value
	 */
	public void calculateNum(int value) {
		this.num = FormulaHelper.executeCeilInt(this.numExpression, value);
	}
	
	/**
	 * 克隆一个未计算的
	 */
	public RandomExprRewardObject clone() {
		RandomExprRewardObject exprRewardObject = new RandomExprRewardObject();
		exprRewardObject.id = this.id;
		exprRewardObject.num = 0;
		exprRewardObject.numExpression = this.numExpression;
		exprRewardObject.rate = this.rate;
		exprRewardObject.rewardType = this.rewardType;
		return exprRewardObject;
	}
	
}
