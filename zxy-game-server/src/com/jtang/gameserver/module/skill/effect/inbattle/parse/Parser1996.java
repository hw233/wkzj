package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.DeadAction;
import com.jtang.gameserver.module.battle.model.DisapperAction;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.type.BufferType;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.SkillReport;
import com.jtang.gameserver.module.skill.model.TargetReport;
import com.jtang.gameserver.module.skill.type.ProcessType;

/**
 * 鹿拼
 * @author vinceruan
 *
 */
@Component
public class Parser1996 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1996;
	}

	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		if (targets.isEmpty()) {
			return false;
		}
		byte distance = 0;
		if (context.getProcessType() == ProcessType.COMMON_ATK) {
			distance = getDistance(caster, targets.get(0), context);
		}
		SkillReport fr = new SkillReport(caster.getFighterId(), effect.getSkillId(), distance);
		
		String expr[] = effect.getEffectExpr().split(Splitable.ELEMENT_SPLIT);
		//生命值减少多少
		int hpDesc = Integer.valueOf(expr[0]);
		//攻击力增加多少
		int deltAtk = FormulaHelper.executeCeilInt(expr[1], caster.getBaseAtk());
		deltAtk = ensureAddValidAtk(deltAtk);
		
		int totalLost = caster.getTotalHpLost();
		if (totalLost == 0 || totalLost < hpDesc) {
			return false;
		}
		String attrKey = "Effect996_calculatedHurt";
		Object calculatedHurtObj = caster.getFightSessionAttributes().get(attrKey);
		if (calculatedHurtObj == null) {
			calculatedHurtObj = 0;
		}
		int calculatedHurt = (int)calculatedHurtObj;
		//计算倍率
		Double timesResult = Math.floor((totalLost - calculatedHurt)/hpDesc);
		int times = timesResult.intValue();
		if (times  == 0) {
			return false;
		}
		int addAtk = times*deltAtk;
		TargetReport report = new TargetReport(caster.getFighterId());
		fr.targets.add(report);
		
		addBuffer(caster, caster, report, AttackerAttributeKey.ATK, effect, context, addAtk, true, BufferType.ATTR_BUFFER);
		caster.getFightSessionAttributes().put(attrKey, totalLost);
		if (caster.isDead()) {
			DeadAction deadAction = new DeadAction(caster.getFighterId());
			report.actions.add(deadAction);
			DisapperAction disapperAction = new DisapperAction(caster.getFighterId());
			report.actions.add(disapperAction);
			deadAction.setDisapperAction(disapperAction);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]增加了攻击力值:[{}]", caster.getName(), effect.getSkillName(), caster.getName(), addAtk);
		}
				
		addSkillReport(effect, fr, context);
		return true;
	}

}
