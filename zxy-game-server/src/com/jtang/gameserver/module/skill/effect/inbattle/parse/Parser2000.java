package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.DeadAction;
import com.jtang.gameserver.module.battle.model.DisapperAction;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.SkillReport;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * @author vinceruan
 * 
 */
@Component
public class Parser2000 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser2000;
	}

	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		Fighter enemy =	context.getTargetEnermy();
		if (!enemy.isDead()) {
			return false;
		}
		//爆炸光效是从死亡者身上发出
		List<Byte> atks = new ArrayList<>();
		atks.add(caster.getFighterId());
		atks.add(enemy.getFighterId());
		SkillReport skillRt = new SkillReport(atks, effect.getSkillId());
		for (Fighter e : targets) {
			if (e.isDead()) {
				continue;
			}
			TargetReport report = new TargetReport(e.getFighterId());
			skillRt.targets.add(report);
			int atk = caster.getAtk();
			int def = e.getDefense();
			addHurtComputedByTwoArg(caster, e, report, effect, context, atk, def);
			if (e.isDead()) {
				DeadAction deadAction = new DeadAction(e.getFighterId());
				report.actions.add(deadAction);
				DisapperAction disapperAction = new DisapperAction(e.getFighterId());
				report.actions.add(disapperAction);
				deadAction.setDisapperAction(disapperAction);
			}
		}
		addSkillReport(effect, skillRt, context);
		return true;
	}
}
