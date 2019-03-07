package com.jtang.worldserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.rhino.FormulaHelper;

/**
 * 最强势力排名相关配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "crossBattleRankConfig")
public class CrossBattleRankConfig implements ModelAdapter {

	/**
	 * 最强势力名次
	 */
	private int powerRank;
	
	/**
	 * 计算总数表达式 (x1:仙人总击  x2:仙人总防御 x3:仙人总生命)
	 */
	private String totalValueExpr;
	
	/**
	 * 攻击当前名次额外增加伤害百分比1-100
	 */
	private int hurtPercent;
	
	/**
	 * 杀死当前名次玩家额外增加伤害百分比(当前生命上限 * 百分比)
	 */
	private int killPercent;
	
	@Override
	public void initialize() {
	}

	public int getPowerRank() {
		return powerRank;
	}

	public int getTotalValueExpr(int totalAtk, int totalHp, int totalDefence) {
		return FormulaHelper.executeCeilInt(totalValueExpr, totalAtk, totalHp, totalDefence);
	}

	public int getHurtPercent() {
		return hurtPercent;
	}

	public int getKillPercent() {
		return killPercent;
	}

}
