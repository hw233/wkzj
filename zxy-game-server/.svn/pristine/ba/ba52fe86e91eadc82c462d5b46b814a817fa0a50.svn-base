package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.helper.FightProcessor;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.PositionAction;
import com.jtang.gameserver.module.battle.model.Tile;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * @author ludd
 *
 */
@Component
public class Parser19941 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser19941;
	}

	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		
		//身后是否有空位
		Tile tile = caster.getCamp().getBehindPosition(caster.getTile());
		boolean isAvailable = context.battleMap.isWalk(tile);
		if (!isAvailable) {
			report.valid = false;
			return false;
		}
		
		//尝试闪避
		processSkillBeforeBeAtked(target, context);
		boolean im = tryImmunity(caster, target, effect, context);
		boolean dodge = FightProcessor.getInstance().tryDodge(target, context);
		if(im || dodge) {
			return true;
		}
		
		String[] strs = StringUtils.split(effect.getEffectExpr(), Splitable.ELEMENT_SPLIT);
		AttackerAttributeKey key = AttackerAttributeKey.getByCode(Byte.valueOf(strs[0]));
		AttackerAttributeKey targetKey = AttackerAttributeKey.getByCode(Byte.valueOf(strs[2]));
		String expr = strs[1];
		
		if (key.equals(AttackerAttributeKey.ATK)) {
			int value = -FormulaHelper.executeInt(expr, caster.getAtk());
			if (targetKey.equals(AttackerAttributeKey.ATK)) {
				this.addAtkBuffer(caster, target, report, value, effect, context);
			} else if (targetKey.equals(AttackerAttributeKey.DEFENSE)) {
				this.addDeffendsBuffer(caster, target, report, value, effect, context);
			} else {
				LOGGER.error(String.format("不支持的属性id:[%s]", key.getCode()));
				report.valid = false;
				return false;
			}
			
		} else if (key.equals(AttackerAttributeKey.DEFENSE)) {
			int value = -FormulaHelper.executeInt(expr, caster.getDefense());
			if (targetKey.equals(AttackerAttributeKey.ATK)) {
				this.addAtkBuffer(caster, target, report, value, effect, context);
			} else if (targetKey.equals(AttackerAttributeKey.DEFENSE)){
				this.addDeffendsBuffer(caster, target, report, value, effect, context);
			} else {
				LOGGER.error(String.format("不支持的属性id:[%s]", key.getCode()));
				report.valid = false;
				return false;
			}
		} else {
			LOGGER.error(String.format("不支持的属性id:[%s]", key.getCode()));
			report.valid = false;
			return false;
		}
		
		//将目标拖到身后
		if (!target.isDead()) {
			context.battleMap.jump(target.getTile(), tile);
			target.setTile(tile);
			report.actions.add(PositionAction.valueOf(target.getFighterId(), tile));
		}
		
		return true;
	}
}
