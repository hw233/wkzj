package com.jtang.gameserver.module.story.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.jtang.core.utility.StringUtils;

public class StoryVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7948398772033902555L;

	/**
	 * 故事id
	 */
	public int storyId;
	
	/**
	 * 战斗结果(0星,1星,2星，3星)
	 */
	public byte storyStar;
	
	/**
	 * 是否已经领取关卡全通关的奖励(0:没有 1:已经领取)
	 */
	public byte oneStarAwarded;
	
	/**
	 * 是否已经领取过2星奖励(0:没有 1:已经领过)
	 */
	public byte twoStarAwarded;
	
	/**
	 * 是否已经领取过3星奖励(0:没有 1:已经领过)
	 */
	public byte threeStarAwarded;
	
	/**
	 * 下面每一个关卡的战斗结果
	 * <pre>
	 * 格式是:Map<BattleId, Star>
	 * </pre>
	 */
	private Map<Integer, Integer> battleStarMap = new TreeMap<Integer, Integer>();

	/**
	 * 是否已领取活力，0：未领取，1：已领取
	 */
	public byte vitRewarded;
	
	/**
	 * 是否领取物品
	 * 0.未领取 1.已领取
	 */
	public byte rewardGoods;
	
	/**
	 * 获取战场的星级
	 * @param battleId
	 * @return
	 */
	public Byte getBattleStar(int battleId) {
		Integer star = this.battleStarMap.get(battleId);
		if (star == null) {
			return -1;
		}
		return star.byteValue();
	}
	
	/**
	 * 当前战斗结果是否比最近一次在该战场的开打记录好
	 * @param battleId	战场id
	 * @param newStar	当前最新的星级
	 * @return
	 */
	public boolean isBetterStar(int battleId, int newStar) {
		return newStar > getBattleStar(battleId);
	}
	
	/**
	 * 添加战斗星级
	 * @param battleId
	 * @param newStar
	 */
	public void putBattleStar(int battleId, Byte newStar) {
		Byte oldStar = getBattleStar(battleId);
		if (oldStar != null && oldStar >= newStar) {
			return;
		}
		battleStarMap.put(battleId, newStar.intValue());
	}

	/**
	 * 获取星级列表
	 * @return
	 */
	public Map<Integer,Integer> getBattleMap() {
		return battleStarMap;
	}
	
	/**
	 * 是否打过这个战场id
	 * @param battleId
	 * @return
	 */
	public boolean containBattleId(int battleId) {
		return battleStarMap.containsKey(battleId);
	}
	
	public static StoryVO valueOf(String[] storyArray) {
		StoryVO vo = new StoryVO();
		storyArray = StringUtils.fillStringArray(storyArray, 8, "0");
		vo.storyId = Integer.valueOf(storyArray[0]);
		vo.oneStarAwarded = Byte.valueOf(storyArray[1]);
		vo.twoStarAwarded = Byte.valueOf(storyArray[2]);
		vo.threeStarAwarded = Byte.valueOf(storyArray[3]);
		vo.storyStar = Byte.valueOf(storyArray[4]);
		Map<Integer, Integer> map = StringUtils.keyValueString2IntMap(storyArray[5]);
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			vo.putBattleStar(entry.getKey(), entry.getValue().byteValue());
		}
		vo.vitRewarded = Byte.valueOf(storyArray[6]);
		vo.rewardGoods = Byte.valueOf(storyArray[7]);
		return vo;
	}
	
}
