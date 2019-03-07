package com.jtang.gameserver.module.app.model.extension.rulevo;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardType;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
/**
 * 兑换物品结构
 * 字符串格式:物品id_数量_兑换目标物品类型(0:物品，1：装备，2：仙人魂魄 ，3：金币)_兑换目标物品id_目标物品数量
 * @author ludd
 *
 */
public class ExchangeGoods {

	/**
	 * 物品id
	 */
	public int goodsId;
	/**
	 * 数量
	 */
	public int goodsNum;
	/**
	 * 兑换目标物品类型
	 */
	public RewardType targetRewardType;
	
	/**
	 * 兑换目标物品id
	 */
	public int targetId;
	
	/**
	 * 目标物品数量
	 */
	public int targetNum;
	
	public String parse2String() {
		List<Object> list = new ArrayList<>();
		list.add(goodsId);
		list.add(goodsNum);
		list.add(targetRewardType.getCode());
		list.add(targetId);
		list.add(targetNum);
		return StringUtils.collection2SplitString(list, Splitable.ATTRIBUTE_SPLIT);
	}
	
	public ExchangeGoods(String[] items) {
		items = StringUtils.fillStringArray(items, 5, "0");
		this.goodsId = Integer.valueOf(items[0]);
		this.goodsNum = Integer.valueOf(items[1]);
		this.targetRewardType = RewardType.getType(Integer.valueOf(items[2]));
		this.targetId = Integer.valueOf(items[3]);
		this.targetNum = Integer.valueOf(items[4]);
	}
}
