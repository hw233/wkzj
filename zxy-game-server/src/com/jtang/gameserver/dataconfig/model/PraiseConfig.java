package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;

/**
 * 好评配置
 * @author ludd
 *
 */
@DataFile(fileName = "praiseConfig")
public class PraiseConfig implements ModelAdapter {
	/**
	 * 装备强化等级
	 */
	private int equipUpgradeNum;
	/**
	 * 抢夺次数
	 */
	private int snatchNum;
	/**
	 * 激活奖励
	 */
	private String activeReward;
	/**
	 * 好评奖励
	 */
	private String praiseReward;
	
	@FieldIgnore
	private List<RewardObject> activeRewardList = new ArrayList<>();
	@FieldIgnore
	private List<RewardObject> praiseRewardList = new ArrayList<>();
	@Override
	public void initialize() {
		activeRewardList.clear();
		praiseRewardList.clear();
		
		List<String[]> active = StringUtils.delimiterString2Array(activeReward);
		for (String[] strings : active) {
			activeRewardList.add(RewardObject.valueOf(strings));
		}
		List<String[]> praise = StringUtils.delimiterString2Array(praiseReward);
		for (String[] strings : praise) {
			praiseRewardList.add(RewardObject.valueOf(strings));
		}
		
		this.activeReward = null;
		this.praiseReward = null;
	}
	public int getEquipUpgradeNum() {
		return equipUpgradeNum;
	}
	public int getSnatchNum() {
		return snatchNum;
	}
	public List<RewardObject> getActiveRewardList() {
		 List<RewardObject> list = new ArrayList<>();
		 list.addAll(activeRewardList);
		return list;
	}
	
	public List<RewardObject> getPraiseRewardList() {
		 List<RewardObject> list = new ArrayList<>();
		 list.addAll(praiseRewardList);
		return list;
	}

	

}
