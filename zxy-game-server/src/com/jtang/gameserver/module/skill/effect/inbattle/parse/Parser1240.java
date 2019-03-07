package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.dataconfig.service.SkillService;
import com.jtang.gameserver.module.battle.helper.FightProcessor;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.model.FighterBuffer;
import com.jtang.gameserver.module.buffer.type.BufferType;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * A往B身上添加一个buffer(该buffer在客户端看不到)，buffer跳动时引发A释放另外一个技能
 * @author vinceruan
 *
 */
@Component
public class Parser1240 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1240;
	}

	@Override
	protected boolean castSkill(final Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, final Context context) {
		report.valid = false;
		if (target.isDead()) {			
			return false;
		}
		
		final int effectId = Integer.parseInt(effect.getEffectExpr());
		
		FighterBuffer buffer = new FighterBuffer(context.generateBufferId(), -1, AttackerAttributeKey.NONE, caster, target, effect, false, BufferType.STATUS_BUFFER) {
			@Override
			protected boolean heartBeatInternal() {
				InbattleEffectConfig conf = SkillService.getInbattleEffect(effectId);
				FightProcessor.getInstance().processSkillEffect(caster, context, conf);
				return true;
			}
		};
		target.addBuffer(buffer);
		return true;
	}
}
