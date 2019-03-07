package com.jtang.core.model;

import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.StringUtils;

public class ExprRewardObject extends RewardObject {
	private static final long serialVersionUID = -1382895177369224880L;

	/**
	 * 数量表达式
	 */
	private String numExpression;
	
	public static ExprRewardObject valueOf(String[] record) {
		record = StringUtils.fillStringArray(record, 3, "0");
		ExprRewardObject rewardObject = new ExprRewardObject();
		rewardObject.rewardType = RewardType.getType(Integer.valueOf(record[0]));
		rewardObject.id = Integer.valueOf(record[1]);
		rewardObject.numExpression = record[2];
		rewardObject.num = 0;
		return rewardObject;
	}
	
	/**
	 * 克隆
	 * @param numExpr	数量参数
	 * @return
	 */
	public RewardObject clone(Number... numExpr) {
		RewardObject rewardObject = new RewardObject();
		rewardObject.rewardType = this.rewardType;
		rewardObject.id = this.id;
		rewardObject.num = FormulaHelper.executeCeilInt(this.numExpression, numExpr);
		return rewardObject;
	}

}
