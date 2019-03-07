package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.model.FighterBuffer;
import com.jtang.gameserver.module.buffer.type.BufferType;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

@Component
public class Parser3000 extends AbstractInBattleEffectParser {

	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		if (caster.isDead()) {
			return false;
		}
		if (target.isDead()) {
			return false;
		}
		FighterBuffer buffer = newBuffer(context.generateBufferId(), caster, target, AttackerAttributeKey.IMMUNITY, effect, 1, false, BufferType.STATUS_BUFFER);
		target.addBuffer(buffer);
		return true;
	}
	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser3000;
	}

}
