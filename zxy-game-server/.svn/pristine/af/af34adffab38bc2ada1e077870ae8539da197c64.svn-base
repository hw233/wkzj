package com.jtang.gameserver.module.battle.model;

import java.util.HashMap;
import java.util.Map;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.module.battle.type.BattleType;

/**
 * 攻打怪物事件
 * @author vinceruan
 *
 */
public class AttackMonsterRequest extends BattleRequest {
	/**
	 * 怪物阵型,格式: Map<LineupGridIndex, HeroVO>, LineupGridIndex为英雄在3*3阵型中的索引值
	 */
	public Map<Integer, MonsterVO> defTeam = new HashMap<Integer, MonsterVO>();
	
	/**
	 * 怪物的属性加成,格式是: Map<SpriteId, Map<AttackerAttributeKey, Value>>
	 * (可选参数)
	 */
	private Map<Long, Map<AttackerAttributeKey, Integer>> defTeamAttrChange = new HashMap<>();
	
	/**
	 * 怪物的总气势
	 */
	public int monsterMorale;
	/**
	 * 是否跳过首回合战斗
	 */
	public boolean skipFirstRound = false;
	
	/**
	 * <pre>
	 * 只包含必填参数的构造函数
	 * @param eventKey 		事件的key
	 * @param map			地图配置
	 * @param actorId		攻击者的id
	 * @param atkTeam		攻击者的队伍
	 * @param defTeam		被攻击者的队伍
	 * @param morale		攻击方的气势
	 * @param monsterMorale	被攻击方的气势
	 * @param args 			可选参数,可以为null
	 * </pre>
	 */
	public AttackMonsterRequest(String eventKey, MapConfig map, long actorId, LineupFightModel atkLineup, 
			Map<Integer, MonsterVO> defTeam, int morale, int monsterMorale, Object args, BattleType battleType) {
		this.eventKey = eventKey;
		this.map = map;
		this.actorId = actorId;
		this.atkTeam = atkLineup.getHeros();
		this.defTeam = defTeam;
		this.morale = morale;
		this.monsterMorale = monsterMorale;
		this.args = args;
		this.mergeAttributeMap(atkTeamAttrChange, atkLineup.getAttributeChanges());
		this.battleType = battleType;
	}
	
	/**
	 * <pre>
	 * 包含可选参数的构造函数
	 * @param eventKey 			事件的key
	 * @param map				地图配置
	 * @param actorId			玩家的id
	 * @param atkTeam			攻击者的队伍
	 * @param defTeam			怪物队伍
	 * @param morale			攻击方的气势
	 * @param monsterMorale		怪物方的气势
	 * @param atkTeamAttrChange	攻击方的属性加成,可选参数,可以为null
	 * @param defTeamAttrChange	怪物方的属性加成,可选参数,可以为null
	 * @param args				可选参数,可以为null
	 * </pre>
	 */
	public AttackMonsterRequest(String eventKey, MapConfig map, long actorId, LineupFightModel atkLineup,
			Map<Integer, MonsterVO> defTeam, int morale, int monsterMorale, 
		Map<Long, Map<AttackerAttributeKey, Integer>> atkTeamAttrChange, 
		Map<Long, Map<AttackerAttributeKey, Integer>> defTeamAttrChange, Object args, BattleType battleType) {

		this(eventKey, map, actorId, atkLineup, defTeam, morale, monsterMorale, args, battleType);
		this.addAtkTeamAttrChange(atkTeamAttrChange);
		this.addDefTeamAttrChange(defTeamAttrChange);
	}
	
	/**
	 * 构造函数
	 * @param eventKey
	 * @param map
	 * @param actorId
	 * @param atkLineup
	 * @param defTeam
	 * @param morale
	 * @param monsterMorale
	 * @param atkTeamAttrChange
	 * @param defTeamAttrChange
	 * @param args
	 */
	public AttackMonsterRequest(String eventKey, MapConfig map, long actorId, Map<Integer, HeroVO> atkLineup,
			Map<Integer, MonsterVO> defTeam, int morale, int monsterMorale, 
		Map<Long, Map<AttackerAttributeKey, Integer>> atkTeamAttrChange, 
		Map<Long, Map<AttackerAttributeKey, Integer>> defTeamAttrChange, Object args, BattleType battleType) {
		this.eventKey = eventKey;
		this.map = map;
		this.actorId = actorId;
		this.atkTeam = atkLineup;
		this.defTeam = defTeam;
		this.morale = morale;
		this.monsterMorale = monsterMorale;
		this.args = args;
		this.addAtkTeamAttrChange(atkTeamAttrChange);
		this.addDefTeamAttrChange(defTeamAttrChange);
		this.battleType = battleType;
	}

	public Map<Long, Map<AttackerAttributeKey, Integer>> getDefTeamAttrChange() {
		return defTeamAttrChange;
	}

	public void addDefTeamAttrChange(Map<Long, Map<AttackerAttributeKey, Integer>> defTeamAttrChange) {
		this.mergeAttributeMap(this.defTeamAttrChange, defTeamAttrChange);
	}
}
