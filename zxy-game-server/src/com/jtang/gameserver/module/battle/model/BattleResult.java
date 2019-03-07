package com.jtang.gameserver.module.battle.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.protocol.StatusCode;
import com.jtang.gameserver.module.battle.type.BattleVO;
import com.jtang.gameserver.module.goods.model.GoodsVO;

/**
 * 战斗结果封装类
 * @author vinceruan
 *
 */
public class BattleResult {
	/**
	 * 战斗数据(该部分数据需要下发给客户端)
	 */
	public FightData fightData = new FightData();
	
	/**
	 * 经验值加成表达式
	 * 格式:
	 * Map<ActorId, Map<HeroId, List<Expression>>>
	 */
	public Map<Long, Map<Integer, List<String>>> addExpExpr = new HashMap<>();
	
	
	/** 物品掉落 ,格式是Map<ActorId,List<GoodsVO> */
	public Map<Long, List<GoodsVO>> dropedGoods = new HashMap<>();
	
	/** 金币掉落,格式是Map<ActorId, Gold> */
	public Map<Long, Integer> golds = new HashMap<>();
	
	/** 战斗事件 */
	public BattleRequest battleReq;
	
	/** 处理结果 */
	public short statusCode = StatusCode.SUCCESS;
	
	/** 攻击方的仙人的存活状态，key=仙人全局id, value=当前血量值*/
	public Map<Byte, Integer> atkTeamStatus = new HashMap<>();
	
	/** 防御的仙人的存活状态，key=仙人全局id, value=当前血量值*/
	public Map<Byte, Integer> defenseTeamStatus = new HashMap<>();
	
	/**
	 * 战斗数据
	 */
	public BattleVO battleVO;

	/**
	 * 防御方
	 */
	public List<Fighter> defendsTeam;

	/**
	 * 攻击方
	 */
	public List<Fighter> attackTeam;
}
