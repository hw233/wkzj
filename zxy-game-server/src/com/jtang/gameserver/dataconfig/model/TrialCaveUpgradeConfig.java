package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "trialCaveUpgradeConfig")
public class TrialCaveUpgradeConfig implements ModelAdapter {
	/**
	 * 当前等级
	 */
	private int level;
	
	/**
	 * 当前等级的最大可试炼次数
	 */
	private int trialLimit;
	
	/**
	 * 升级需要点券数
	 */
	private int upgradeTicket;
	
	/**
	 * 战斗后增加的声望百分比
	 */
	private int battleReputationPercent;
	
	/**
	 * 战斗后获得的物品额外增加数  
	 */
	private int battleGoodsNum;
			
	@Override
	public void initialize() {
	}

	public int getLevel() {
		return level;
	}

	public int getTrialLimit() {
		return trialLimit;
	}
	
	public int getUpgradeTicket() {
		return upgradeTicket;
	}
	
	/**
	 * 计算增加的声望值
	 * @param awardReputation	当前奖励的声望值
	 * @return
	 */
	public int AddReputation(int awardReputation) {
		if(awardReputation < 1) {
			return 0;
		}
		return Math.round(awardReputation * battleReputationPercent / 100);
	}

	public int getBattleGoodsNum() {
		return battleGoodsNum;
	}

}
