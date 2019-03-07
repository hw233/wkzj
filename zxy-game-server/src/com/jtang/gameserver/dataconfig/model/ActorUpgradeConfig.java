package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;

/**
 * 角色升级配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "actorUpgradeConfig")
public class ActorUpgradeConfig implements ModelAdapter {
	
	/**
	 * 角色等级
	 */
	private int level;
	
	/**
	 * 需要声望
	 */
	private long needReputation;
	
	/**
	 * 仙人升级的上限
	 */
	private int heroLevelLimit;
	
	/**
	 * 装备升级的上限
	 */
	private int equipLevelLimit;
	
	/**
	 * 抢夺兑换券上限数
	 */
	private int snatchVoucherLimit;
	
	/**
	 * 奖励
	 */
	private String reward;
	
	/**
	 * 增加的活力上限
	 */
	private int addVitMax;
	
	/**
	 * 奖励物品集合列表
	 */
	@FieldIgnore
	private List<RewardObject> rewardList = new ArrayList<>();
	
	@Override
	public void initialize() {		
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for (String[] item : list) {
			RewardObject rewardObject = RewardObject.valueOf(item);
			rewardList.add(rewardObject);
		}
		
		this.reward = null;
	}

	public int getLevel() {
		return level;
	}

	public long getNeedReputation() {
		return needReputation;
	}

	public int getHeroLevelLimit() {
		return heroLevelLimit;
	}

	public int getEquipLevelLimit() {
		return equipLevelLimit;
	}

	public int getSnatchVoucherLimit() {
		return snatchVoucherLimit;
	}

	public List<RewardObject> getRewardList() {
		return rewardList;
	}
	
	public int getAddVitMax() {
		return addVitMax;
	}
 
}
