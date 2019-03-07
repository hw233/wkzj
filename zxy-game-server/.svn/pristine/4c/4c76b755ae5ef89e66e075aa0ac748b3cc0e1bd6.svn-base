package com.jtang.gameserver.module.hole.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.gameserver.module.battle.model.FightData;

public class HoleBattleResult {
	/**
	 * 战斗数据
	 */
	public FightData fightData;
	/**
	 * 战斗获得星级
	 */
	public byte battleStar;

	/**
	 * 洞府总星级
	 */
	public byte holeStar;

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
	 * 额外的奖励
	 */
	public Map<Byte, Long> awardAttribute = new HashMap<>();

	/**
	 * 仙人获得的经验
	 */
	public Map<Integer, Integer> awardHeroExp;
	
	/**
	 * 洞府自增id
	 */
	public long id;
	
	/**
	 * 洞府关卡id
	 */
	public int holeBattleId;
}
