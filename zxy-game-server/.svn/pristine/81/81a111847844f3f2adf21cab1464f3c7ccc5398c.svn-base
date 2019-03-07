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
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.SkillReport;
import com.jtang.gameserver.module.skill.model.TargetReport;
/**
 * 会给所有目标加属性（攻，防，血）这里的血指的是同时增加临时的生命上限和生命值，增加的次数有上限
 * @author ludd
 *
 */
@Component
public class Parser2004 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser2004;
	}
	
	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		byte distance = 0;
//		if (context.getProcessType() == ProcessType.COMMON_ATK) {
//			distance = getDistance(caster, targets.get(0), context);
//		}
		SkillReport skillReport = new SkillReport(caster.getFighterId(), effect.getSkillId(), distance);
		String[] values = StringUtils.split(effect.getEffectExpr(), Splitable.ELEMENT_SPLIT);
		byte targetType = Byte.valueOf(values[0]);//类型
		int num =  Integer.valueOf(values[2]);//次数上限
		byte casterType = Byte.valueOf(values[3]);
		AttackerAttributeKey key = AttackerAttributeKey.getByCode(targetType);
		AttackerAttributeKey casterKey = AttackerAttributeKey.getByCode(casterType);
		
		for (Fighter fighter : targets) {
			if (fighter.isDead()) {
				continue;
			}
			TargetReport report = new TargetReport(fighter.getFighterId());
			int casterValue = getAddValue(caster, casterKey);
			switch (key) {
			case ATK: {
				if (fighter.hasBufferNum(effect.getEffectId(), key) < num) {
					int value = FormulaHelper.executeInt(values[1], casterValue);
					this.addAtkBuffer(caster, fighter, report, value, effect, context);
				}
				break;
			}
			case DEFENSE: {
				if (fighter.hasBufferNum(effect.getEffectId(), key) < num) {
					int value = FormulaHelper.executeInt(values[1],casterValue);
					this.addDeffendsBuffer(caster, fighter, report, value, effect, context);
				}
				break;
			}
			case HP_MAX: {
				if (fighter.hasBufferNum(effect.getEffectId(), key) < num) {
					int value = FormulaHelper.executeInt(values[1], casterValue);
					incrHpMax(caster, fighter, report, effect, context, value);
				}
				break;
			}
				

			default:
				LOGGER.error(String.format("解析数值错误，类型:[%s]", targetType));
				break;
			}
			skillReport.targets.add(report);
		}
		
		addSkillReport(effect, skillReport, context);
		return true;
	}
	
	private int getAddValue(Fighter caster, AttackerAttributeKey key) {
		int value = 0;
		switch (key) {
		case ATK: 
				value = caster.getBaseAtk();
			break;
		case DEFENSE: 
				value = caster.getBaseDefense();
			break;
		case HP_MAX: 
				value = caster.getBaseHpMax();
			break;
			
		default:
			LOGGER.error(String.format("解析数值错误，类型:[%s]", key.getCode()));
			break;
		}
		
		return value;
	}
	
}
