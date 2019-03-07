package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.BattleMap;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.DeadAction;
import com.jtang.gameserver.module.battle.model.DisapperAction;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.RepulseAction;
import com.jtang.gameserver.module.battle.model.Tile;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.SkillReport;
import com.jtang.gameserver.module.skill.model.TargetReport;
import com.jtang.gameserver.module.skill.type.ProcessType;

/**
 * 羊撞(击退,后面没有敌人才后退,击退不会造成伤害)
 * 目前适用于:
 * <pre>
 * 羊力大仙-羊撞
 * </pre>
 * @author vinceruan
 *
 */
@Component
public class Parser1993 extends AbstractInBattleEffectParser {

	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		if (targets.isEmpty()) {
			return false;
		}
		byte distance = 0;
		if (context.getProcessType() == ProcessType.COMMON_ATK) {
			distance = getDistance(caster, targets.get(0), context);
		}
		SkillReport fr = new SkillReport(caster.getFighterId(), effect.getSkillId(), distance);
		boolean result = false;
		for (Fighter target : targets) {
			if (target.isDead()) {
				continue;
			}
			
			Tile tile = target.getCamp().getBehindPosition(target.getTile());
			BattleMap battleMap = context.getBattleMap();
			if (battleMap.isWalk(tile)) {
				TargetReport report = new TargetReport(target.getFighterId());
				fr.targets.add(report);
				
				battleMap.move(target.getTile(), tile);
				target.setTile(tile);
//				MoveAction move = MoveAction.valueOf(target.getFighterId(), tile);
//				report.actions.add(move);
				report.actions.add(RepulseAction.valueOf(target.getFighterId(), tile));
//				AttributeChange ch = AttributeChange.valueOf(AttackerAttributeKey.POSITION, tile);
//				report.addAttrChange(ch);
				result = true;
				if (target.isDead()) {
					DeadAction deadAction = new DeadAction(target.getFighterId());
					report.actions.add(deadAction);
					DisapperAction disapperAction = new DisapperAction(target.getFighterId());
					report.actions.add(disapperAction);
					deadAction.setDisapperAction(disapperAction);
				}
			}
			
		}
		addSkillReport(effect, fr, context);
		return result;
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1993;
	}

}
