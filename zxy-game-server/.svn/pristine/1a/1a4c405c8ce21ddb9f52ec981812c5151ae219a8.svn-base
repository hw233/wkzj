package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.SkillReport;
import com.jtang.gameserver.module.skill.model.TargetReport;
import com.jtang.gameserver.module.skill.type.ProcessType;

/**
 * 复活并给队友加防御
 * @author ludd
 *
 */
@Component
public class Parser12001 extends AbstractInBattleEffectParser{
	
	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		if (!caster.isDead()) {
			return false;
		}
		String expression = effect.getEffectExpr();
		String[] exprs = StringUtils.split(expression, Splitable.ELEMENT_SPLIT);
		int hp = Math.round(Float.valueOf(exprs[0])* caster.getHpMax());
		caster.setHp(hp);
		TargetReport report = new TargetReport(caster.getFighterId());
		addAttributeChange(caster, report, AttackerAttributeKey.HP);
		context.fighterRevive(caster);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]进行了复活", caster.getName(), effect.getSkillName(), caster.getName());
		}
		byte distance = 0;
		if (context.getProcessType() == ProcessType.COMMON_ATK) {
			distance = getDistance(caster, targets.get(0), context);
		}
		SkillReport skillRt = new SkillReport(caster.getFighterId(), effect.getSkillId(), distance);
		skillRt.targets.add(report);
		List<Fighter> friends = context.getTeamListByCamp(caster.getCamp());
		for (Fighter e : friends) {
			if (e.isDead() || e == caster) {
				continue;
			}
			TargetReport report1 = new TargetReport(e.getFighterId());
			skillRt.targets.add(report1);
			int addDeffens = FormulaHelper.executeInt(exprs[1], caster.getDefense());
			e.addDefense(addDeffens);
			addAttributeChange(e, report1, AttackerAttributeKey.DEFENSE);
		}
		addSkillReport(effect, skillRt, context);
		return true;
	}
	/*
	@Override
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {	
		
		if(!target.isDead()) {
			report.valid = false;
			return false;
		}
		String expression = effect.getEffectExpr();
		String[] exprs = StringUtils.split(expression, Splitable.ELEMENT_SPLIT);
		int hp = Math.round(Float.valueOf(exprs[0])* target.getHpMax());
		target.setHp(hp);
		
		addAttributeChange(target, report, AttackerAttributeKey.HP);
		
		context.fighterRevive(target);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]进行了复活", caster.getName(), effect.getSkillName(), target.getName());
		}
		
		List<Fighter> fighters = context.getTeamListByCamp(target.getCamp());
		trs = new ArrayList<>();
		for (Fighter fighter : fighters) {
			if (fighter.isDead()) {
				continue;
			}
			TargetReport targetReport = new TargetReport(fighter.getFighterId());
			trs.add(targetReport);
			int addDeffens = FormulaHelper.executeInt(exprs[1], caster.getDefense());
//			addDefComputedByOneArg(caster, fighter, targetReport, addDeffens, effect, context);
			addBuffer(caster, fighter, targetReport, AttackerAttributeKey.DEFENSE, effect, context, addDeffens, true, BufferType.ATTR_BUFFER);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("[{}]释放技能[{}],对[{}]增加了防御力:[{}]", caster.getName(), effect.getSkillName(), fighter.getName(), addDeffens);
			}
		}
		return true;
	}
*/
	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser12001;
	}	
	
}
