package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.AttributeChange;
import com.jtang.gameserver.module.battle.model.BufferAction;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.model.FighterBuffer;
import com.jtang.gameserver.module.buffer.type.BufferType;
import com.jtang.gameserver.module.buffer.type.ImmobilezeState;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 致使目标混乱,攻击自己人
 * @author vinceruan
 *
 */
@Component
public class Parser1220 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1220;
	}

	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		processSkillBeforeBeAtked(target, context);
		boolean im = tryImmunity(caster, target, effect, context);
		if (im) {
			return true;
		}
		
		FighterBuffer buffer = newBuffer(context.generateBufferId(), caster, target, AttackerAttributeKey.IN_FIGHTING, effect, ImmobilezeState.CONFUSE.getState(), true, BufferType.STATUS_BUFFER);
		int state = target.addBuffer(buffer);
		
		BufferAction buffAction = BufferAction.addBuffer(target.getFighterId(), buffer.getEffectId(), AttributeChange.valueOf(AttackerAttributeKey.IMMOBILIZE, state));
		report.actions.add(buffAction);
				
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]增加了混乱状态", caster.getName(), effect.getSkillName(), target.getName());
		}
		return true;
	}
}
