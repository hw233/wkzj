package com.jtang.gameserver.module.battle.model;

import java.util.HashMap;
import java.util.Map;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.HeroVO;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.module.battle.type.BattleType;

/**
 * 战斗事件
 * @author vinceruan
 *
 */
public abstract class BattleRequest {
	/**
	 * 事件类型,{@code EventKey}
	 * 战斗结果会通过该eventKey进行事件发布
	 * 所有注册了该事件的接收器都会收到回调
	 */
	public String eventKey;
	
	/**
	 * 玩家角色ID
	 */
	public long actorId;
	
	/**
	 * 地图配置
	 */
	public MapConfig map;
	
	/**
	 * 气势
	 */
	public int morale;
	
	/**
	 * 自己的队伍阵型,格式: Map<LineupGridIndex, HeroVO>, LineupGridIndex为英雄在3*3阵型中的索引值
	 */
	public Map<Integer, HeroVO> atkTeam = new HashMap<Integer, HeroVO>();
	
	/**
	 * 攻击方英雄的属性加成列表(可选参数)
	 */
	protected Map<Long, Map<AttackerAttributeKey, Integer>> atkTeamAttrChange = new HashMap<>();
	
	/**
	 * 其它参数,战斗模块不需要这些参数，只会原样传回给回调接口(可选参数)
	 */
	public Object args;
	
	/**
	 * 战斗类型
	 */
	public BattleType battleType;
	
	protected void mergeAttributeMap(Map<Long, Map<AttackerAttributeKey, Integer>> targetMap, Map<Long, Map<AttackerAttributeKey, Integer>> sourceMap) {
		if (sourceMap == null) {
			return;
		}
		for (Long key : sourceMap.keySet()) {
			Map<AttackerAttributeKey, Integer> attrMap = sourceMap.get(key);
			if (targetMap.containsKey(key) == false) {
				targetMap.put(key, attrMap);
				continue;
			}
			
			for (AttackerAttributeKey attrKey : attrMap.keySet()) {
				Integer val = attrMap.get(attrKey);
				Integer oldVal = targetMap.get(key).get(attrKey);
				if (oldVal == null) {
					oldVal = val;
				} else {
					oldVal += val;
				}
				targetMap.get(key).put(attrKey, oldVal);
			}
		}
	}

	public Map<Long, Map<AttackerAttributeKey, Integer>> getAtkTeamAttrChange() {
		return atkTeamAttrChange;
	}

	public void addAtkTeamAttrChange(Map<Long, Map<AttackerAttributeKey, Integer>> atkTeamAttrChange) {
		this.mergeAttributeMap(this.atkTeamAttrChange, atkTeamAttrChange);
	}
}
