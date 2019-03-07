package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.model.FighterBuffer;
import com.jtang.gameserver.module.buffer.type.BufferType;
import com.jtang.gameserver.module.buffer.type.CycleType;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 提高仙人的射程
 * @author vinceruan
 *
 */
@Component
public class Parser1160 extends AbstractInBattleEffectParser{
	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {		
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		int addHitRange = FormulaHelper.executeInt(effect.getEffectExpr());
		FighterBuffer buffer = newBuffer(context.generateBufferId(), caster, target, AttackerAttributeKey.ATTACK_SCOPE, effect, addHitRange, false, BufferType.ATTR_BUFFER);
		buffer.heartBeat(CycleType.RIGHT_NOW);
		
		addAttributeChange(target, report, AttackerAttributeKey.ATTACK_SCOPE);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]增加了射程:[{}]", caster.getName(), effect.getSkillName(), target.getName(), addHitRange);
		}
		return true;
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1160;
	}
}
