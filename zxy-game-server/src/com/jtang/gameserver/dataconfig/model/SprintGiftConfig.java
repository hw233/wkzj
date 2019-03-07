package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;

/**
 * 冲级礼包配置
 * @author ligang
 *
 */
@DataFile(fileName = "sprintGiftConfig")
public class SprintGiftConfig implements ModelAdapter {

	/**
	 * 礼包领取等级
	 */
	private int level;
	
	/**
	 * 奖励物品
	 */
	private String rewards;
	
	/**
	 * vip权限礼包(赠送vipX就填写X)
	 */
	private int vipLevel;
	
	@FieldIgnore
	private List<RewardObject> rewardsList = new ArrayList<RewardObject>();
	
	@Override
	public void initialize() {
		rewardsList.clear();
		List<String[]> tempList = StringUtils.delimiterString2Array(this.rewards);
		
		for (String[] params : tempList) {
			RewardObject rewardObject = RewardObject.valueOf(params);
			rewardsList.add(rewardObject);
		}
	}

	public int getLevel() {
		return level;
	}

	public int getVipLevel() {
		return vipLevel;
	}
	
	public List<RewardObject> getRewardsList() {
		return rewardsList;
	}

}
