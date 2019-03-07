package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;

/**
 * 冲级礼包VIP权限兑换物品配置
 * @author ligang
 *
 */
@DataFile(fileName = "sprintGiftVIPConvertConfig")
public class SprintGiftVIPConvertConfig implements ModelAdapter {

	/**
	 * VIP等级
	 */
	private int vipLevel;
	
	/**
	 * VIP对应等级可兑换的奖励物品
	 */
	private String rewards;
	
	@FieldIgnore
	private List<RewardObject> convertRewardsList = new ArrayList<RewardObject>();
	
	@Override
	public void initialize() {
		convertRewardsList.clear();
		List<String[]> tempList = StringUtils.delimiterString2Array(this.rewards);
		
		for (String[] params : tempList) {
			RewardObject rewardObject = RewardObject.valueOf(params);
			convertRewardsList.add(rewardObject);
		}
	}


	public int getVipLevel() {
		return vipLevel;
	}
	
	public List<RewardObject> getConvertRewardsList() {
		return convertRewardsList;
	}

}
