package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jtang.gameserver.module.skill.type.ProcessType;
/**
 * 射程范围内躲在敌人身后的敌人只受递减伤害
 * @author ludd
 *
 */
@Component
public class Parser2001 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser2001;
	}

	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		if (caster.isDead()) {
			return false;
		}
		byte distance = 0;
		if (context.getProcessType() == ProcessType.COMMON_ATK) {
			distance = getDistance(caster, targets.get(0), context);
		}
		SkillReport skillRt = new SkillReport(caster.getFighterId(), effect.getSkillId(), distance);
		
		Map<Fighter, Boolean> isheadMap = new HashMap<Fighter, Boolean>();
		for (Fighter fighter : targets) {
			boolean headHasHero = isAhead(fighter, context);
			isheadMap.put(fighter, headHasHero);
		}
		
		for (Fighter target : targets) {
			if (target.isDead()) {
				continue;
			}
			boolean headHasHero = isheadMap.get(target);
			TargetReport report = new TargetReport(target.getFighterId());
			
			int arg = headHasHero? 1 : 0;
			addHurtComputedByThreeArg(caster, target, report , effect, context, caster.getAtk(), target.getDefense(), arg);
			skillRt.targets.add(report);
			if (target.isDead()) {
				DeadAction deadAction = new DeadAction(target.getFighterId());
				report.actions.add(deadAction);
				DisapperAction disapperAction = new DisapperAction(target.getFighterId());
				report.actions.add(disapperAction);
				deadAction.setDisapperAction(disapperAction);
			}
		}
		addSkillReport(effect, skillRt, context);
		return skillRt.isValid();
	}
	/**
	 * 是否前方有一个人
	 * @param target
	 * @param targets
	 * @return
	 */
	protected boolean isAhead(Fighter target, Context context) {
		int aheadNum = getAheadFighter(target, context, true).size();
		if (aheadNum >= 1){
			return true;
		}
		return false;
	}
	
	

	
	

}
