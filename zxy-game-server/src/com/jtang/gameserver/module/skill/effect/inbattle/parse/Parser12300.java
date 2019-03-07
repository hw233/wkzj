package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.PositionAction;
import com.jtang.gameserver.module.battle.model.Tile;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.SkillReport;
import com.jtang.gameserver.module.skill.model.TargetReport;
//import com.jtang.sm2.module.battle.model.AttributeChange;
//import com.jtang.sm2.module.battle.type.AttackerAttributeKey;
import com.jtang.gameserver.module.skill.type.ProcessType;

/**
 * 与防御最低的交换位置
 * @author vinceruan
 *
 */
@Component
public class Parser12300 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser12300;
	}

	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		if (caster.isDead()) {			
			return false;
		}
		
		Fighter target = null;
		for (Fighter fighter : targets) {
			if (fighter.isDead()) {
				continue;
			}
			if (target == null) {
				target = fighter;
			} else {
				if (target.getDefense() > fighter.getDefense()) {
					target = fighter;
				}
			}
		}
		
		if (target == null) {
			return false;
		}
		
		byte distance = 0;
		if (context.getProcessType() == ProcessType.COMMON_ATK) {
			distance = getDistance(caster, target, context);
		}
		SkillReport skillRpt = new SkillReport(caster.getFighterId(), effect.getSkillId(), distance);
		TargetReport tr = new TargetReport(caster.getFighterId());		
		skillRpt.targets.add(tr);
		
		tr = new TargetReport(target.getFighterId());		
		skillRpt.targets.add(tr);
		processSkillBeforeBeAtked(target, context);
		boolean im = tryImmunity(caster, target, effect, context);
		if (im) {
			addSkillReport(effect, skillRpt, context);
			return true;
		}
		
		
		Tile tile1 = caster.getTile();
		Tile tile2 = target.getTile();
		caster.setTile(tile2);
		target.setTile(tile1);
		tr.actions.add(PositionAction.valueOf(caster.getFighterId(), caster.getTile()));
//		AttributeChange ch = AttributeChange.valueOf(AttackerAttributeKey.POSITION, caster.getTile());
//		tr.addAttrChange(ch);
		
		
		tr.actions.add(PositionAction.valueOf(target.getFighterId(), target.getTile()));
//		ch = AttributeChange.valueOf(AttackerAttributeKey.POSITION, target.getTile());
//		tr.addAttrChange(ch);
		
		addSkillReport(effect, skillRpt, context);
		
		return true;
	}
}
