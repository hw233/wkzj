package com.jtang.worldserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;

/**
 * 贡献点兑换配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "crossBattleExchangeConfig")
public class CrossBattleExchangeConfig implements ModelAdapter {

	/**
	 * 兑换奖励唯一id
	 */
	private int exchangeAwardId;
	
	/**
	 * 扣除贡献点
	 */
	private int costExchangePoint;
	
	/**
	 * 奖励物品(每个物品一条) 格式: 类型(0:物品，1：装备，2：仙人魂魄 ，3：金币）_物品id_物品数量
	 */
	private String reward;
	
	@FieldIgnore
	private List<RewardObject> rewardList = new ArrayList<>();
	
	@Override
	public void initialize() {
		rewardList.clear();
		List<String[]> winList = StringUtils.delimiterString2Array(reward);
		for (String[] str : winList) {
			RewardObject obj = RewardObject.valueOf(str);
			rewardList.add(obj);
		}
	}

	public int getExchangeAwardId() {
		return exchangeAwardId;
	}

	public int getCostExchangePoint() {
		return costExchangePoint;
	}
	
	/**
	 * 获取奖励列表
	 * @param actorLevel
	 * @return
	 */
	public List<RewardObject> getRewardList() {
		return rewardList;
	}

}
