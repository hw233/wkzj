package com.jtang.gameserver.module.battle.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.jtang.gameserver.module.battle.model.Action;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.CompositeAction;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.battle.model.ReviveAction;
import com.jtang.gameserver.module.battle.model.RoundResult;
import com.jtang.gameserver.module.battle.model.SequenceAction;
import com.jtang.gameserver.module.battle.model.SkillAction;
import com.jtang.gameserver.module.battle.model.SkillTarget;
import com.jtang.gameserver.module.battle.model.SpawnAction;
import com.jtang.gameserver.module.battle.type.Camp;
import com.jtang.gameserver.module.goods.model.GoodsVO;

/**
 * <pre>
 * 战报的结构比较复杂，所以用单独一个类来管理
 * 大体来说,战报的结构如下:
 * + 一场战斗:包含N个回合的战斗情况,用{@code FightData}表示
 * ++ 一个回合:包含每个人的出手情况,用{@code RoundResult}表示
 * +++ 一次出手:包含普通攻击、技能攻击、buffer的变化、仙人移动，被击者的技能反击,用{@code Action}表示
 * ++++ 普通攻击和技能攻击:需要下发攻击方、被攻击方和属性变化,用{@code SkillAction}表示
 * ++++ buffer变化:需要下发受影响人、效果id,用{@code BufferAction}表示
 * ++++ 仙人移动:需要下发仙人和目标坐标,用{@code MoveAction}表示
 * </pre>
 * 
 * @author vinceruan
 *
 */
public class FightResultRecorder {
	/**
	 * 战斗结果
	 */
	private BattleResult fightResult = new BattleResult();
		
	public Stack<Action> compositeActionStack = new Stack<>();
	
	/**
	 * 当前回合
	 */
	public RoundResult currentRound;
	
	public List<List<Action>> skillActions = new ArrayList<>();
	
	
	public BattleResult getFightResult() {
		check(fightResult.fightData);
		return fightResult;
	}
	
	//将RoundResult中的atkResults一些无用的Action清除
	public void check(FightData data) {
		for (RoundResult round : data.roundResults) {
			List<Action> list = round.atkResults;
			round.atkResults = new ArrayList<>();
			for (Action oldAct : list) {
				Action newAct = check(oldAct);
				if (newAct != null) {
					round.atkResults.add(newAct);
				}
			}
		}
	}
	
	public Action check(Action action) {
		if (action == null) {
			return null;
		}

		if (action instanceof SequenceAction) {
			SequenceAction seqAct = (SequenceAction)action;
			List<Action> list = seqAct.actions;
			seqAct.actions = new ArrayList<>();
			for (Action oldAct : list) {
				Action newAct = check(oldAct);
				if (newAct != null) {
					seqAct.actions.add(newAct);
				}
			}
			if (seqAct.actions.size() == 0) {
				return null;
			} if (seqAct.actions.size() == 1) {
				return seqAct.actions.get(0);
//				return seqAct;
			} else {
				return seqAct;
			}
		}
		
		if (action instanceof SpawnAction) {
			SpawnAction spawnAct = (SpawnAction)action;
			List<Action> list = spawnAct.actions;
			spawnAct.actions = new ArrayList<>();
			for (Action oldAct : list) {
				Action newAct = check(oldAct);
				if (newAct != null) {
					spawnAct.actions.add(newAct);
				}
			}
			if (spawnAct.actions.size() == 0) {
				return null;
			} if (spawnAct.actions.size() == 1) {
				return spawnAct.actions.get(0);
//				return spawnAct;
			} else {
				return spawnAct;
			}
		}
		return action;
	}
	
	/**
	 * 开始记录开战前的buffer变动
	 */
	public void startRecordBufferBeforeFight() {
		this.compositeActionStack.push(this.fightResult.fightData.buffersBeforFight);
	}
	
	/**
	 * 停止记录开战前的buffer变动
	 */
	public void stopRecordBufferBeforeFight() {
		this.compositeActionStack.pop();
	}
	
	/**
	 * 开始记录战斗结束时的buffer变动
	 */
	public void startRecordBufferAfterFight() {
		SequenceAction action = new SequenceAction();
		this.compositeActionStack.push(action);
	}
	
	/**
	 * 停止记录战斗结束时的buffer变动
	 */
	public void stopRecordBufferAfterFight() {
		this.compositeActionStack.pop();
	}
	
	/**
	 * 开始记录一个回合
	 */
	public void startRecordRound() {
		currentRound = new RoundResult();
		this.fightResult.fightData.roundResults.add(currentRound);
	}	
	
	/**
	 * 开始记录回合前的buffer变动
	 */
	public void startRecordBufferBeforeRound() {
		SequenceAction action = this.currentRound.buffersBeforRound;
		this.compositeActionStack.push(action);
	}
	
	/**
	 * 停止记录回合前的buffer变动
	 */
	public void stopRecordBufferBeforeRound() {
		this.compositeActionStack.pop();
	}
	
	/**
	 * 开始记录回合结束时的buffer变动
	 */
	public void startRecordBufferAfterRound() {
		SequenceAction action = this.currentRound.buffersAfterRound;
		this.compositeActionStack.push(action);
	}
	
	/**
	 * 停止记录回合结束时的buffer变动
	 */
	public void stopRecordBufferAfterRound() {
		this.compositeActionStack.pop();
	}
	
	/**
	 * 开始记录一次出手
	 */
	public void startRecordOneAtk() {
		SequenceAction action = new SequenceAction();
		this.compositeActionStack.push(action);
		this.currentRound.atkResults.add(action);
	}
	
	/**
	 * 停止记录一次出手
	 */
	public void stopRecordOneAtk() {
		this.compositeActionStack.pop();
	}
	
	/**
	 * 开始记录技能释放:产生SkillAction
	 * @param fighterId
	 * @param skillId
	 */
	public void startRecordSkillAtk(List<Byte> fighterIds, int skillId, List<SkillTarget> targets, List<Action> triggerActions, byte distance, List<Action> actions) {
		if (targets.isEmpty()) {
			return;
		}
		SkillAction action = SkillAction.valueOf(fighterIds, skillId, targets, triggerActions, distance);
		this.link2Parent(action);
		skillActions.add(actions);
		return;
	}
	
	public void recordRevive(ReviveAction reviveAction) {
		this.link2Parent(reviveAction);
	}
	public void recordAction(Action action) {
		this.link2Parent(action);
	}
	
	/**
	 * 开始记录并行的action
	 */
	public void startSpwanAction() {
		SpawnAction action = new SpawnAction();
		this.link2Parent(action);
		this.compositeActionStack.push(action);
	}
	
	/**
	 * 开始记录顺序的action
	 */
	public void startSequenceAction() {
		SequenceAction action = new SequenceAction();
		this.link2Parent(action);
		this.compositeActionStack.push(action);
	}
	
	public void stopSequenceAction() {
		this.compositeActionStack.pop();
	}
	
	/**
	 * 停止记录并行的action
	 */
	public void stopSpwanAction() {
		this.compositeActionStack.pop();
	}
	
	private void link2Parent(Action action) {
		CompositeAction actoin = (CompositeAction)this.compositeActionStack.peek();
		actoin.add(action);
	}
	
	/**
	 * 记录物品掉落
	 * @param action
	 */
	public void addDropGoodsAction(Action action) {
		this.link2Parent(action);
	}
	
	/**
	 * 添加buffer
	 * @param buffer
	 * @param isAttachBuff
	 */
	public void addAction(Action action) {
		this.link2Parent(action);
	}
	
	public void addActions(List<Action> actions) {
		for (Action action : actions) {
			this.link2Parent(action);
		}
	}
	
	public void addDropedGoods(long actorId, GoodsVO goods) {
		List<GoodsVO> list = this.fightResult.dropedGoods.get(actorId);
		if (list == null) {
			list = new ArrayList<>();
			this.fightResult.dropedGoods.put(actorId, list);
		}
		list.add(goods);
	}
	
	public void addDropedGold(long actorId, int count) {
		Integer num = this.fightResult.golds.get(actorId);
		if (num == null) {
			num = 0;
		}
		num += count;
		this.fightResult.golds.put(actorId, num);
	}
	
	public FightData getFightData() {
		return this.fightResult.fightData;
	}
	
	public void addAwardExpExpr(long actorId, int heroId, String expr) {		
		Map<Integer, List<String>> map = this.fightResult.addExpExpr.get(actorId);
		if (map == null) {
			map = new HashMap<Integer, List<String>>();
			this.fightResult.addExpExpr.put(actorId, map);
		}
		List<String> list = map.get(heroId);
		if (list == null) {
			list = new ArrayList<>();
			map.put(heroId, list);
		}
		list.add(expr);
	}
	
	public void setFirstAtkTeam(Camp camp) {
		this.fightResult.fightData.firstAtkTeam = (byte)(camp == Camp.ABOVE ? 1 : 2);
	}
}
