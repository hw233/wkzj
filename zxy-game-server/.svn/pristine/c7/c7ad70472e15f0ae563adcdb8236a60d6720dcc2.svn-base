package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.PositionAction;
import com.jtang.gameserver.module.battle.model.Tile;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 让目标前移或后移
 * @author vinceruan
 *
 */
@Component
public class Parser1231 extends AbstractInBattleEffectParser {

	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		
		int step = Integer.parseInt(effect.getEffectExpr());
		Tile tile = target.getCamp().jump(target.getTile(), step);
		boolean result = context.battleMap.move(target.getTile(), tile);
		if (result) {
//			MoveAction move = MoveAction.valueOf(target.getFighterId(), tile);
//			report.actions.add(move);			
			target.setTile(tile);
			report.actions.add(PositionAction.valueOf(target.getFighterId(), target.getTile()));
//			AttributeChange ch = AttributeChange.valueOf(AttackerAttributeKey.POSITION, target.getTile());
//			report.addAttrChange(ch);
			return true;
		}
		
		return true;
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1231;
	}	
}
