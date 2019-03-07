package com.jtang.gameserver.module.treasure.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.gameserver.module.battle.model.FightData;

public class TreasureBattleResult {
	/**
	 * 战斗数据
	 */
	public FightData fightData;
	/**
	 * 战斗获得星级
	 */
	public byte battleStar;

	/**
	 * 装备掉落
	 */
	public List<Long> equipList = new ArrayList<>();

	/**
	 * 仙人魂魄掉落
	 */
	public Map<Integer, Integer> herosoulMap = new HashMap<>();

	/**
	 * 物品掉落
	 */
	public Map<Long, Integer> awardGoods = new HashMap<>();
	
	/**
	 * 金币掉落
	 */
	public Map<Byte, Integer> awardAttribute = new HashMap<>();

	/**
	 * 仙人获得经验
	 */
	public int addHeroExp;
}
