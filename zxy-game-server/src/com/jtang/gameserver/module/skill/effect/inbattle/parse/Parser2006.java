package com.jtang.gameserver.module.skill.effect.inbattle.parse;


import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.type.BufferType;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;
/**
 * 标记
 * @author ludd
 *
 */
@Component
public class Parser2006 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser2006;
	}
	
	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		if (target.isMark()) {
			report.valid = false;
			return false;
		}
		addBuffer(caster, target, report, AttackerAttributeKey.MARK, effect, context, 1, false, BufferType.STATUS_BUFFER);
		
		return true;
	}

}
