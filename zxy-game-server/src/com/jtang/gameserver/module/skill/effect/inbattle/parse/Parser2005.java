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
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.SkillReport;
import com.jtang.gameserver.module.skill.model.TargetReport;
import com.jtang.gameserver.module.skill.type.ProcessType;
/**
 * 给前方的友方临时提高生命，给后方的友方加攻击, 提高值由施法者属性决定
 * @author ludd
 *
 */
@Component
public class Parser2005 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser2005;
	}
	
	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		if (caster.isDead()) {
			return false;
		}
		List<Fighter> aHeadFriends = getAheadFighter(caster, context, true);
		List<Fighter> behandFriends = getBehindFighter(caster, context, true);
		
		
		if (aHeadFriends.isEmpty() && behandFriends.isEmpty()) {
			return false;
		}
		String[] values = StringUtils.split(effect.getEffectExpr(), Splitable.ELEMENT_SPLIT);
		byte type = Byte.valueOf(values[0]);
		
		AttackerAttributeKey key = AttackerAttributeKey.getByCode(type);
		int value = 0;
		switch (key) {
		case ATK: {
			value = FormulaHelper.executeInt(values[1], caster.getBaseAtk());
			break;
		}
		case DEFENSE: {
			value = FormulaHelper.executeInt(values[1], caster.getBaseDefense());
			break;
		}
		case HP_MAX: {
			value = FormulaHelper.executeInt(values[1], caster.getBaseHpMax());
			break;
		}

		default:
			LOGGER.error(String.format("解析数值错误，类型:[%s]", type));
			return false;
		}
		byte distance = 0;
		if (context.getProcessType() == ProcessType.COMMON_ATK) {
			distance = getDistance(caster, targets.get(0), context);
		}
		SkillReport skillReport = new SkillReport(caster.getFighterId(), effect.getEffectId(), distance);
		for (Fighter aHeadFriend : aHeadFriends) {
			TargetReport report = new TargetReport(aHeadFriend.getFighterId());
			skillReport.targets.add(report);
			addHp(caster, aHeadFriend, report, effect, context, value);
		}
		
		for (Fighter behandFriend : behandFriends) {
			TargetReport report = new TargetReport(behandFriend.getFighterId());
			skillReport.targets.add(report);
			AttackBuffer fighterBuffer = new AttackBuffer(context.generateBufferId(), value, caster, behandFriend, effect);
			this.addBuffer(fighterBuffer, behandFriend, effect, context, report);
		}
		
		addSkillReport(effect, skillReport, context);
		return true;
	}

}
