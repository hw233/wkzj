package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.constant.BattleRule;
import com.jtang.gameserver.module.battle.helper.FightProcessor;
import com.jtang.gameserver.module.battle.model.AttributeChange;
import com.jtang.gameserver.module.battle.model.BufferAction;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.model.FighterBuffer;
import com.jtang.gameserver.module.buffer.type.BufferType;
import com.jtang.gameserver.module.buffer.type.CycleType;
import com.jtang.gameserver.module.buffer.type.ImmobilezeState;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 伤害+定身
 * @author vinceruan
 *
 */
@Component
public class Parser1171 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1171;
	}

	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		
		processSkillBeforeBeAtked(target, context);
		
		//尝试闪避
		boolean im = tryImmunity(caster, target, effect, context);
		boolean dodge = FightProcessor.getInstance().tryDodge(target, context);
		if(im || dodge) {
			return true;
		}
		
		int atk = caster.getAtk();		
		int defense = target.getDefense();
		String[] exp = StringUtils.split(effect.getEffectExpr(), Splitable.ELEMENT_SPLIT);
		int hurt = FormulaHelper.executeCeilInt(exp[1], atk, defense, BattleRule.BATTLE_DEF_FACTOR);
		
		hurt = ensureValidHurt(hurt);
		
		//减血
		target.addHurt(hurt);
		
		//记录战报
		this.addAttributeChange(target, report, AttackerAttributeKey.HP);
		
		//记录受影响者(用于反击处理)
		context.addFighterBeAtcked(target);
		
		//记录阵型的总伤害值(用于对峙时胜败判断)
		context.addAtkHur(caster.getCamp(), hurt);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] 释放技能[{}], 对[{}]造成伤害值:[{}]", caster.getName(), effect.getSkillName(), target.getName(), hurt);
		}
		
		//以下是定身
		if(target.isDead() == false) {
			ImmobilezeState immobilezeState = ImmobilezeState.getByState(Byte.valueOf(exp[0]));
			FighterBuffer buffer = newBuffer(context.generateBufferId(), caster, target, AttackerAttributeKey.IMMOBILIZE, effect, immobilezeState.getState(), true, BufferType.STATUS_BUFFER);
			buffer.heartBeat(CycleType.RIGHT_NOW);
			int state = target.addBuffer(buffer);
			
			BufferAction buffAction = BufferAction.addBuffer(target.getFighterId(), buffer.getEffectId(), AttributeChange.valueOf(AttackerAttributeKey.IMMOBILIZE, state));
			report.actions.add(buffAction);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("[{}]释放技能[{}],对[{}]进行了定身", caster.getName(), effect.getSkillName(), target.getName());
			}
		}
		
		return true;
	}

}
