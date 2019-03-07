package com.jtang.gameserver.module.battle.model;

import java.util.HashMap;
import java.util.Map;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.module.battle.type.BattleType;

public class AttackPlayerRequest extends BattleRequest {
	/**
	 * 敌方的队伍阵型,格式: Map<LineupGridIndex, HeroVO>, LineupGridIndex为英雄在3*3阵型中的索引值
	 */
	public Map<Integer, HeroVO> defTeam;
	
	/**
	 * 防御方英雄的属性加成列表(可选参数)
	 */
	private Map<Long, Map<AttackerAttributeKey, Integer>> defTeamAttrChange = new HashMap<>();
	
	/**
	 * 被攻击的玩家
	 */
	public long targetActorId;
	
	/**
	 * 对方的气势
	 */
	public int targetMorale;
	
	public boolean continueHP = false;
	
	/**
	 * <pre>
	 * 构造函数:必填参数
	 * @param eventKey 			事件的key, 参考{@code EventKey}
	 * @param map				地图配置
	 * @param actorId			攻击方的角色id
	 * @param atkTeam			攻击方的队伍
	 * @param targetActorId		防御方的角色id
	 * @param defTeam			防御方的队伍
	 * @param morale			攻击方的气势
	 * @param targetMorale		防御方的气势
	 * @param args				其它参数,可以为null
	 * <pre>
	 */
	public AttackPlayerRequest(String eventKey, MapConfig map, long actorId, LineupFightModel atkLineup, 
			long targetActorId, LineupFightModel defLineup, int morale, int targetMorale, Object args, BattleType battleType) {
		this.eventKey = eventKey;
		this.map = map;
		this.actorId = actorId;
		this.atkTeam = atkLineup.getHeros();
		this.targetActorId = targetActorId;
		this.defTeam = defLineup.getHeros();
		this.morale = morale;
		this.targetMorale = targetMorale;
		this.args = args;
		this.addAtkTeamAttrChange(atkLineup.getAttributeChanges());
		this.addDefTeamAttrChange(defLineup.getAttributeChanges());
		this.battleType = battleType;
	}
	
	/**
	 * 构造函数
	 * @param eventKey
	 * @param map
	 * @param actorId
	 * @param atkLineup
	 * @param targetActorId
	 * @param defLineup
	 * @param morale
	 * @param targetMorale
	 * @param atkTeamAttrChange
	 * @param defTeamAttrChange
	 * @param args
	 */
	public AttackPlayerRequest(String eventKey, MapConfig map, long actorId, Map<Integer, HeroVO> atkLineup, 
			long targetActorId, Map<Integer, HeroVO> defLineup, int morale, int targetMorale,
			Map<Long, Map<AttackerAttributeKey, Integer>> atkTeamAttrChange, 
			Map<Long, Map<AttackerAttributeKey, Integer>> defTeamAttrChange,
			Object args, BattleType battleType) {
		this.eventKey = eventKey;
		this.map = map;
		this.actorId = actorId;
		this.atkTeam = atkLineup;
		this.targetActorId = targetActorId;
		this.defTeam = defLineup;
		this.morale = morale;
		this.targetMorale = targetMorale;
		this.args = args;
		this.addAtkTeamAttrChange(atkTeamAttrChange);
		this.addDefTeamAttrChange(defTeamAttrChange);
		this.battleType = battleType;
	}
	
	/**
	 * <pre>
	 * 包含可选参数的构造函数
	 * @param eventKey 			事件的key, 参考{@code EventKey}
	 * @param map				地图配置
	 * @param actorId			攻击方的角色id
	 * @param atkLineup			攻击方的队伍
	 * @param targetActorId		防御方的角色id
	 * @param defLineup			防御方的队伍
	 * @param morale			攻击方的气势
	 * @param targetMorale		防御方的气势
	 * @param atkTeamAttrChange	攻击方的属性加成,可选参数,可以为null
	 * @param defTeamAttrChange 防御方的属性加成,可选参数,可以为null
	 * @param args				其它参数,可以为null
	 * <pre>
	 */
	public AttackPlayerRequest(String eventKey, MapConfig map, long actorId, LineupFightModel atkLineup,
			long targetActorId, LineupFightModel defLineup, int morale, int targetMorale,
			Map<Long, Map<AttackerAttributeKey, Integer>> atkTeamAttrChange, 
			Map<Long, Map<AttackerAttributeKey, Integer>> defTeamAttrChange, Object args, BattleType battleType) {
		this(eventKey, map, actorId, atkLineup, targetActorId, defLineup, morale, targetMorale, args, battleType);
		this.addAtkTeamAttrChange(atkLineup.getAttributeChanges());
		this.addDefTeamAttrChange(defLineup.getAttributeChanges());
	}

	public Map<Long, Map<AttackerAttributeKey, Integer>> getDefTeamAttrChange() {
		return defTeamAttrChange;
	}

	public void addDefTeamAttrChange(Map<Long, Map<AttackerAttributeKey, Integer>> defTeamAttrChange) {
		this.mergeAttributeMap(this.defTeamAttrChange, defTeamAttrChange);
	}
}
