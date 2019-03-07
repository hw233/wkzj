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
 * 增加普攻次数
 * @author vinceruan
 *
 */
@Component
public class Parser1190 extends AbstractInBattleEffectParser{
	@Override
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		report.valid = false;
		if (target.isDead()) {			
			return false;
		}
		int addAtkTimes = FormulaHelper.executeInt(effect.getEffectExpr());
		FighterBuffer buffer = newBuffer(context.generateBufferId(), caster, target, AttackerAttributeKey.COMMON_ATTACK_TIMES, effect, addAtkTimes, false, BufferType.STATUS_BUFFER);
		target.addBuffer(buffer);
		buffer.heartBeat(CycleType.RIGHT_NOW);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]增加了普通次数:[{}]", caster.getName(), effect.getSkillName(), target.getName(), addAtkTimes);
		}
		
		return true;
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1190;
	}	
}
