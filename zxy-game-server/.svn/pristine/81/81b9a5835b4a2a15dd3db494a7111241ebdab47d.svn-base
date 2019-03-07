package com.jtang.gameserver.module.skill.effect.inbattle.parse;


import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;
@Component
public class Parser4000 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser4000;
	}
	
	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		report.valid = false;
		if (target.isDead()) {
			return false;
		}
		int moveNum = Integer.valueOf(effect.getEffectExpr());
		moveNum = moveNum <= 1 ? 1 : moveNum;
		target.moveNum  = moveNum;
		
		return true;
	}

}
