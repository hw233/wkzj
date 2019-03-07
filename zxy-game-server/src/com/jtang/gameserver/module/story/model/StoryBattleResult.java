package com.jtang.gameserver.module.story.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.gameserver.module.battle.model.FightData;

/**
 * 故事里一个关卡的战斗结果
 * @author vinceruan
 *
 */
public class StoryBattleResult {

	/**
	 * 战斗数据
	 */
	public FightData fightData;
	
	/**
	 * 物品掉落
	 */
	public Map<Long, Integer> awardGoods = new HashMap<>();
	
	/**
	 * 仙人获得的经验
	 */
	public Map<Integer, Integer> awardHeroExp;
	
	/**
	 * 奖励的装备
	 */
	public List<Long> equips = new ArrayList<>();
	
	/**
	 * 奖励的角色属性值
	 */
	public Map<Byte, Number> awardAttribute = new HashMap<>();
	
	/**
	 * 奖励的英雄魂魄数,格式是Map<魂魄ID,数量>
	 */
	public Map<Integer, Integer> awardHeroSouls = new HashMap<>();
}
