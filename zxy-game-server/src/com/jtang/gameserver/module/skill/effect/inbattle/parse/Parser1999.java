package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.rhino.FormulaHelper;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.constant.BattleRule;
import com.jtang.gameserver.module.battle.helper.BattleHelper;
import com.jtang.gameserver.module.battle.helper.FightProcessor;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.DeadAction;
import com.jtang.gameserver.module.battle.model.DisapperAction;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.SkillReport;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 大巫穿云箭
 * @author vinceruan
 *
 */
@Component
public class Parser1999 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1999;
	}

	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		if (targets.isEmpty() || isAllDead(targets)) {
			return false;
		}
		
		Fighter reference = targets.get(0);
		for (Fighter f : targets) {
			if (f.isDead()) {
				continue;
			}
			if (reference.isDead()) {
				reference = f;
			}
			if(f.getCamp().isBehind(reference.getTile(), f.getTile())) {
				reference = f;
			}
		}
		
		//计算弓箭经过的友方
		List<Fighter> friends = context.getTeamListByCamp(caster.getCamp());
		int friendNum = BattleHelper.getFighterInLine(caster.getTile(), reference.getTile(), friends).size();
		int atk = caster.getAtk();
		byte distance = 0;
		SkillReport sr = new SkillReport(caster.getFighterId(), effect.getSkillId(), distance);
		for (Fighter tar : targets) {
			if (tar.isDead()){
				continue;
			}
			TargetReport report = new TargetReport(tar.getFighterId());
			sr.targets.add(report);
			processSkillBeforeBeAtked(tar, context);
			
			//尝试闪避
			boolean im = tryImmunity(caster, tar,  effect, context);
			boolean dodge = FightProcessor.getInstance().tryDodge(tar, context);
			if(im || dodge) {
				continue;
			}
			int hurt = FormulaHelper.executeCeilInt(effect.getEffectExpr(), atk, friendNum, tar.getDefense(), BattleRule.BATTLE_DEF_FACTOR);
			hurt = ensureValidHurt(hurt);
			addActualHurt(caster, tar, report, effect, context, hurt);
			
			if ( tar.isDead()) {
				DeadAction deadAction = new DeadAction(tar.getFighterId());
				report.actions.add(deadAction);
				DisapperAction disapperAction = new DisapperAction(tar.getFighterId());
				report.actions.add(disapperAction);
				deadAction.setDisapperAction(disapperAction);
			}
		}
		addSkillReport(effect, sr, context);
		return true;
	}
	
	private boolean isAllDead(List<Fighter> list) {
		for (Fighter fighter : list) {
			if (!fighter.isDead()) {
				return false;
			}
		}
		return true;
	}
}
