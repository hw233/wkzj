package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.rhino.FormulaHelper;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.DropActorPropertyAction;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.Tile;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;

/**
 * 银角贪婪
 * @author vinceruan
 *
 */
@Component
public class Parser1992 extends AbstractInBattleEffectParser {
	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {		
		if (targets.isEmpty()) {
			return false;
		}
		for (Fighter target : targets) {
//			int num = Integer.parseInt(effect.getEffectExpr());
			if (caster.getLevel() == 0) {
				continue;
			}
			double enemyLevel = target.getLevel();
			double casterLevel = caster.getLevel();
//			Double result = Double.valueOf(enemyLevel*num*enemyLevel/casterLevel);
			int count = FormulaHelper.executeCeilInt(effect.getEffectExpr(), casterLevel, enemyLevel);
						
			if (count > 0) {
				context.fightRecorder.addDropedGold(caster.actorId, count);
				
				Tile tile = target.getTile();					
				DropActorPropertyAction action = new DropActorPropertyAction(tile, ActorAttributeKey.GOLD, count);
				context.fightRecorder.addDropGoodsAction(action);
				
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("[{}]释放技能[{}],[{}]身上掉落了金币,数量[{}]", caster.getName(), effect.getSkillName(), target.getName(), count);
				}
			}
		}
		return true;
	}

	@Override
	protected int getParserId() {	
		return InbattleParserKey.Parser1992;
	}

}
