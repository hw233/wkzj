package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.ReviveAction;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 复活
 * @author vinceruan
 *
 */
@Component
public class Parser1200 extends AbstractInBattleEffectParser{
	@Override
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {		
		if(!target.isDead()) {
			report.valid = false;
			return false;
		}
		context.fightRecorder.recordRevive(new ReviveAction(target.getFighterId()));
		int hp = Math.round(Float.valueOf(effect.getEffectExpr()) * target.getHpMax());
		target.setHp(hp);
		
		addAttributeChange(target, report, AttackerAttributeKey.HP);
//		report.actions.add(new ReviveAction(target.getFighterId()));
//		if (context.battleMap.isWalk(target.getTile()) == false) {
//			Tile tile = context.battleMap.getRandomRoadTile();
//			report.actions.add(TeleportAction.valueOf(target.getFighterId(), tile));
//			target.setTile(tile);
//		} 
		
		context.fighterRevive(target);
		
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]进行了复活", caster.getName(), effect.getSkillName(), target.getName());
		}
		return true;
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1200;
	}	
}
