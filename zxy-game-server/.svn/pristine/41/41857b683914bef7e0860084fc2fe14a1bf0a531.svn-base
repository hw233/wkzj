package com.jtang.gameserver.dataconfig.model;

import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "treasureExchangeConfig")
public class TreasureExchangeConfig implements ModelAdapter {

	/**
	 * 兑换id
	 */
	public int exchangeId;

	/**
	 * 兑换物品 id_num
	 */
	public String deductGoods;

	/**
	 * 兑换目标物品
	 */
	public String exchangeGoods;

	@FieldIgnore
	public int deductId;
	
	@FieldIgnore
	public int deductNum;

	@FieldIgnore
	private RewardObject rewardObject;

	@Override
	public void initialize() {
		List<Integer> deductList = StringUtils.delimiterString2IntList(deductGoods, Splitable.ATTRIBUTE_SPLIT);
		deductId = deductList.get(0);
		deductNum = deductList.get(1);

		List<Integer> exchangeList = StringUtils.delimiterString2IntList(exchangeGoods, Splitable.ATTRIBUTE_SPLIT);
		RewardType rewardType = RewardType.getType(exchangeList.get(0));
		int id = exchangeList.get(1);
		int num = exchangeList.get(2);
		rewardObject = new RewardObject(rewardType, id, num);
		
		this.deductGoods = null;
		this.exchangeGoods = null;
	}
	
	public RewardObject getReward(){
		return rewardObject;
	}

}