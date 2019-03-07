package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.rhino.FormulaHelper;
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
 * 自己生命低于一定比例时，与后排换位
 * @author vinceruan
 *
 */
@Component
public class Parser12301 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser12301;
	}

	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		if (caster.isDead()) {
			return false;
		}
		
		int maxHp = caster.getHpMax();
		int leastHp = FormulaHelper.executeInt(effect.getEffectExpr(), maxHp);
		if (caster.getHp() >= leastHp) {
			return false;
		}
		
		Fighter target = null;
		
		
		Tile tile1 = caster.getTile();
		Tile tile2 = caster.getCamp().getBehindPosition(tile1);
		List<Fighter> friends = context.getTeamListByCamp(caster.getCamp());
		for (Fighter fighter : friends) {
			if (fighter.getTile().equals(tile2)){
				target = fighter;
			}
		}
		if (target == null || target.isDead()) {			
			return false;
		}
		
		caster.setTile(tile2);
		target.setTile(tile1);
		byte distance = 0;
		if (context.getProcessType() == ProcessType.COMMON_ATK) {
			distance = getDistance(caster, target, context);
		}
		SkillReport skillRpt = new SkillReport(caster.getFighterId(), effect.getSkillId(), distance);
		TargetReport tr = new TargetReport(caster.getFighterId());		
		skillRpt.targets.add(tr);
		tr.actions.add(PositionAction.valueOf(caster.getFighterId(), caster.getTile()));
//		AttributeChange ch = AttributeChange.valueOf(AttackerAttributeKey.POSITION, caster.getTile());
//		tr.addAttrChange(ch);
		
		tr = new TargetReport(target.getFighterId());		
		skillRpt.targets.add(tr);
		tr.actions.add(PositionAction.valueOf(target.getFighterId(), target.getTile()));
//		ch = AttributeChange.valueOf(AttackerAttributeKey.POSITION, target.getTile());
//		tr.addAttrChange(ch);
		
		addSkillReport(effect, skillRpt, context);
		
		return true;
	}
}
