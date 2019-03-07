package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;

/**
 * 
 * 提高仙人的经验收获(在基础经验上面加成)
 * @author vinceruan
 *
 */
@Component
public class Parser1120 extends AbstractInBattleEffectParser{

//	@Override
//	public boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
//		report.valid = false;
//		/**
//		 * 因为战斗模块不负责经验结算,所以这里只把经验值加成表达式缓存起来,转给其它模块处理.
//		 */
//		String expr = effect.getEffectExpr();
//		context.fightRecorder.addAwardExpExpr(target.actorId, target.getHeroId(), expr);
//		
//		return true;
//	}
	
	

	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		String expr = effect.getEffectExpr();
		for (Fighter fighter : targets) {
			context.fightRecorder.addAwardExpExpr(caster.actorId, fighter.getHeroId(), expr);
		}
		return true;
		
	}



	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1120;
	}
}
