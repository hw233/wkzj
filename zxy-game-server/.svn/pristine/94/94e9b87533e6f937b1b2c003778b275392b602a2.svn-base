package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName="rechargeConfig")
public class RechargeConfig implements ModelAdapter {
	
	/**
	 * 充值次数
	 */
	public int rechargeNum;
	
	/**
	 * 赠送的奖励
	 */
	public String giveReward;
	
	@FieldIgnore
	public List<RewardObject> rewardList = new ArrayList<>();

	@Override
	public void initialize() {
		List<String[]> list = StringUtils.delimiterString2Array(giveReward);
		for(String[] str:list){
			RewardObject rewadObject = RewardObject.valueOf(str);
			rewardList.add(rewadObject);
		}
		giveReward = null;
	}

}
