package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.constant.BattleRule;
import com.jtang.gameserver.module.battle.model.BattleMap;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.DeadAction;
import com.jtang.gameserver.module.battle.model.DisapperAction;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.MoveAction;
import com.jtang.gameserver.module.battle.model.RepulseAction;
import com.jtang.gameserver.module.battle.model.Tile;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 敲击(击退目标,若目标后方有敌人则造成伤害,并且弹回到原来的位置,否则只是后退一格)
 * @author vinceruan
 *
 */
@Component
public class Parser1991 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1991;
	}

	@Override
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		
		int effectId  = effect.getEffectId();
		
		int addHurt = FormulaHelper.executeCeilInt(effect.getEffectExpr(), caster.getAtk(), target.getDefense(), BattleRule.BATTLE_DEF_FACTOR);
		addHurt = ensureValidHurt(addHurt);
		
		BattleMap battleMap = context.getBattleMap();
		Tile fromTile = target.getTile();
		Tile tile = target.getCamp().getBehindPosition(target.getTile());
		
		
		addHurt(target, report, context, addHurt, effectId);
		// 超出地图范围不能后退.
		if (battleMap.isOutOfMap(tile)) {
			return true;
		}

		// 可行走
		if (battleMap.isWalk(tile)) {
			repulseTo(target, report, context, battleMap, tile);
			return true;
		} else { // 不能行走，后面有英雄
			// 目标格子一定有人,查看是否是敌人.
			List<Fighter> list = context.getTeamListByCamp(caster.getCamp().getEnermyCamp());
			for (Fighter hero : list) {
				if (hero.isDead()) {
					continue;
				}
				if (hero.getTile().equals(tile)) {
					repulseTo(target, report, context, battleMap, tile);
					// 后方仙人因碰撞受伤记录
					TargetReport targetReport = new TargetReport(hero.getFighterId());
					addHurt(hero, targetReport, context, addHurt, effectId);
					if (hero.isDead()) {
						DeadAction deadAction = new DeadAction(hero.getFighterId());
						targetReport.actions.add(deadAction);
						DisapperAction disapperAction = new DisapperAction(hero.getFighterId());
						targetReport.actions.add(disapperAction);
						deadAction.setDisapperAction(disapperAction);
					}
					if (hero.isDead() == false) {
						moveTo(target, report, context, battleMap, fromTile);
					}
					context.tempTargetReports.add(targetReport);
					return true;
				}
			}
			// 目标格子是队友
			return true;
		}
	}

	/**
	 * @param target
	 * @param context
	 * @param addHurt
	 */
	protected void addHurt(Fighter target, TargetReport report, Context context, int addHurt, int effectId) {		
		target.addHurt(addHurt);	
		addAttributeChange(target, report, AttackerAttributeKey.HP);
		context.addFighterBeAtcked(target);
	}

	private boolean repulseTo(Fighter target, TargetReport report, Context context, BattleMap battleMap, Tile tile) {
		boolean flag = battleMap.move(target.getTile(), tile);
		target.setTile(tile);
		RepulseAction move = RepulseAction.valueOf(target.getFighterId(), tile);
		report.actions.add(move);
		return flag;
	}
	private boolean moveTo(Fighter target, TargetReport report, Context context, BattleMap battleMap, Tile tile) {
//		if (target.isDead()) {
//			return false;
//		}
		boolean flag = battleMap.move(target.getTile(), tile);
		target.setTile(tile);
		MoveAction move = MoveAction.valueOf(target.getFighterId(), tile);
		report.actions.add(move);
		return flag;
	}
	

	
}
