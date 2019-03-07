package com.jtang.gameserver.module.skill.effect.inbattle.parse;


import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;
@Component
public class Parser5000 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser5000;
	}
	
	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		int hurt = target.getHp();
		//减血
		target.addHurt(Math.abs(hurt));
		
		//记录战报
		this.addAttributeChange(target, report, AttackerAttributeKey.HP);
		
		//记录受影响者(用于反击)
		context.addFighterBeAtcked(target);
		
		//统计阵营的总伤害值(用于对峙时胜败判定)
		context.addAtkHur(caster.getCamp(), hurt);
		return true;
	}

}
