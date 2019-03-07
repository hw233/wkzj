package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

/**
 * 洞府配置
 * 
 * @author jianglf
 * 
 */
@DataFile(fileName = "holeConfig")
public class HoleConfig implements ModelAdapter {
	/**
	 * 洞府id
	 */
	public int holeId;

	/**
	 * 洞府内的战场id
	 */
	public String holeBattleId;

	/**
	 * 通关奖励(暂时只有物品非金币点券) 1星_类型_id_数量|2星_类型_id_数量|3星_类型_id_数量
	 */
	public String reward;

	/**
	 * 盟友全部通关大礼包 类型(0.物品 1.装备 2.魂魄 3.金币)_id(为金币填0)_数量
	 */
	public String gift;

	/**
	 * 洞府持续时间
	 */
	public int time;

	@FieldIgnore
	private List<Integer> holeBattleIdList = new ArrayList<Integer>();
	@FieldIgnore
	private Map<Integer, HoleRewardConfig> rewardMap = new HashMap<Integer, HoleRewardConfig>();
	@FieldIgnore
	private List<HoleRewardConfig> giftList = new ArrayList<HoleRewardConfig>();

	@Override
	public void initialize() {

		holeBattleIdList = StringUtils.delimiterString2IntList(holeBattleId, Splitable.ELEMENT_SPLIT);

		List<String[]> rewards = StringUtils.delimiterString2Array(reward);
		for (String[] str : rewards) {
			rewardMap.put(Integer.valueOf(str[0]), HoleRewardConfig.parseHoleReward(str));
		}

		List<String[]> gifts = StringUtils.delimiterString2Array(gift);
		for (String[] str : gifts) {
			giftList.add(HoleRewardConfig.parseAllyGift(str));
		}
		
		this.holeBattleId = null;
		this.reward = null;
		this.gift = null;
	}

	public int getHoleBattleNum() {
		return holeBattleIdList.size();
	}

	public HoleRewardConfig getRewardConfig(int star) {
		return rewardMap.get(star);
	}

	public List<HoleRewardConfig> getHoleGiftList() {
		return giftList;
	}

	public Set<Integer> getAllRewardStar() {
		return rewardMap.keySet();
	}

}
