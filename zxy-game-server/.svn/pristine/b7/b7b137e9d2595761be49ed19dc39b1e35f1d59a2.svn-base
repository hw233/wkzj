package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.model.AttackBuffer;
import com.jtang.gameserver.module.buffer.model.DeffendsBuffer;
import com.jtang.gameserver.module.buffer.model.FighterBuffer;
import com.jtang.gameserver.module.buffer.model.HertBuffer;
import com.jtang.gameserver.module.buffer.type.BufferEffectType;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;
/**
 * 每回合减少属性值
 * @author ludd
 *
 */
@Component
public class Parser2003 extends AbstractInBattleEffectParser {

	
	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		String expr = effect.getEffectExpr();
		String[] values = StringUtils.split(expr, Splitable.ELEMENT_SPLIT);
		int type = Integer.valueOf(values[0]);
		int value = 0;
		BufferEffectType bufferEffectType = BufferEffectType.valueOf(type);
		if (bufferEffectType == null) {
			report.valid = false;
			return false;
		}
		switch (bufferEffectType) {
			case DECREASE_ATTACK: {
				value = FormulaHelper.executeInt(values[1], target.getBaseAtk());
				decraseValue(caster, target, report, value, effect, context, AttackerAttributeKey.ATK);
				break;
			}
			case DECREASE_DEFFENSE: {
				value = FormulaHelper.executeInt(values[1], target.getBaseDefense());
				decraseValue(caster, target, report, value, effect, context, AttackerAttributeKey.DEFENSE);
				break;
			}
			case DECREASE_HP: {
				value = FormulaHelper.executeInt(values[1], target.getBaseHpMax());
				decraseValue(caster, target, report, value, effect, context, AttackerAttributeKey.HP);
				break;
			}
			case NOT_MOVE: {
	
				break;
			}
				
	
			default:
				report.valid = false;
				LOGGER.error(String.format("解析数值错误，类型:[%s]", type));
				return false;
		}
		return true;
	}
	private boolean decraseValue(Fighter caster, Fighter target, TargetReport report, int value, InbattleEffectConfig effect, Context context, AttackerAttributeKey key) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		
		value = -Math.abs(value);
		if (value == 0) {
			value = -1;
		}
		FighterBuffer fighterBuffer = null;
		switch (key) {
		case ATK: {
			if (value + target.getAtk() < 0) {
				value = -Math.abs(target.getBaseAtk());
			}
			fighterBuffer = new AttackBuffer(context.generateBufferId(), value, caster, target, effect);
			break;
		}
		case DEFENSE: {
			if (value + target.getDefense() < 0) {
				value = -Math.abs(target.getBaseDefense());
			}
			fighterBuffer = new DeffendsBuffer(context.generateBufferId(), value, caster, target, effect);
			break;
		}
		case HP: {
			if (value + target.getHpMax()< 0) {
				value = -Math.abs(target.getBaseHpMax());
			}
			fighterBuffer = new HertBuffer(context.generateBufferId(), value, key, caster, target, effect, context);
			break;
		}
			

		default:
			return false;
		}
		
		List<FighterBuffer> buffers = target.getBuffers().get(key);
		if (buffers != null) {
			for (FighterBuffer fb : buffers) {
				if (fb.getEffectId() == effect.getEffectId()) { //对同一个人不重复放
					value += fb.getAddVal();
					fb.setAddVal(value);
					return true;
				}
			}
		}
				
		this.addBuffer(fighterBuffer, target, effect, context, report);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]减少了属性[{}]值:[{}]", caster.getName(), effect.getSkillName(), target.getName(), key, value);
		}
		
		return true;
		
	}
	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser2003;
	}

}
