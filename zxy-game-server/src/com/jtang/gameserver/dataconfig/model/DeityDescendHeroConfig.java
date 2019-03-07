package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RandomExprRewardObject;
import com.jtang.core.model.RandomRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
/**
 * 天神下凡仙人配置
 * @author lig
 *
 */
@DataFile(fileName = "deityDescendHeroConfig")
public class DeityDescendHeroConfig implements ModelAdapter {

	/**
	 *仙人配置id
	 */
	public int heroId;
	
	/**
	 * 转换魂魄数量
	 */
	public int convertNum;

	/**
	 * 砸蛋奖励概率物品
	 */
	private String hitReward;
	
	/**
	 * 砸蛋点亮字的几率
	 */
	private String hitProb;
	
	/**
	 * 砸蛋保底
	 */
	private String hitUseNum;
	
	/**
	 * 砸蛋一次价格
	 */
	public int price1;
	
	/**
	 * 砸蛋十次价格
	 */
	public int price10;

	@FieldIgnore
	public List<RandomExprRewardObject> hitRewardObjectsList = null;
	
	@FieldIgnore
	public List<Integer> hitProbList = null;
	
	@FieldIgnore
	public List<Integer> hitUseNumList = null;
	
	
	@Override
	public void initialize() {
		hitRewardObjectsList = new ArrayList<RandomExprRewardObject>();
		List<String> list = StringUtils.delimiterString2List(hitReward, Splitable.ELEMENT_SPLIT);
		for (String str : list) {
			String[] value = StringUtils.split(str, Splitable.ATTRIBUTE_SPLIT);
			RandomExprRewardObject e = RandomExprRewardObject.valueOf(value);
			hitRewardObjectsList.add(e);
		}
		hitRewardObjectsList = Collections.unmodifiableList(hitRewardObjectsList);
		this.hitReward = null;
		
		hitProbList = new ArrayList<Integer>();
		hitUseNumList = new ArrayList<Integer>();
		List<Integer> probList = StringUtils.delimiterString2IntList(hitProb, Splitable.ATTRIBUTE_SPLIT);
		for (int prob : probList) {
			hitProbList.add(prob);
		}
		List<Integer> useList = StringUtils.delimiterString2IntList(hitUseNum, Splitable.ATTRIBUTE_SPLIT);
		for (int use : useList) {
			hitUseNumList.add(use);
		}
		hitProbList = Collections.unmodifiableList(hitProbList);
		useList = Collections.unmodifiableList(useList);
	}
	
	public List<RewardObject> getHitRewardList(int actorLevel) {
		Map<Integer, Integer> ids = new HashMap<Integer, Integer>();
		for (int i = 0; i < hitRewardObjectsList.size(); i++) {
			RandomRewardObject rewardObject = hitRewardObjectsList.get(i);
			ids.put(i, rewardObject.rate);
		}
		
		Integer id = RandomUtils.randomHit(1000, ids);
		if (id == null) {
			return new ArrayList<RewardObject>();
		} else {
			RandomExprRewardObject reward = hitRewardObjectsList.get(id).clone();
			reward.calculateNum(actorLevel);
			RewardObject rewardObject = new RewardObject(reward.rewardType, reward.id, reward.num);
			List<RewardObject> list = new ArrayList<RewardObject>();
			list.add(rewardObject);
			return list;
		}
		
	}
	
	
}
