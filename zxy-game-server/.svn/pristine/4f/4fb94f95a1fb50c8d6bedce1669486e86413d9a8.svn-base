package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.model.FighterBuffer;
import com.jtang.gameserver.module.buffer.type.BufferType;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.SkillReport;
import com.jtang.gameserver.module.skill.model.TargetReport;
import com.jtang.gameserver.module.skill.type.ProcessType;

/**
 * 免疫
 * @author ludd
 *
 */
@Component
public class Parser1181 extends AbstractInBattleEffectParser{
	
	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		if (targets.isEmpty()) {
			return false;
		}
		
		//保存一次技能达到N个人身上的数据包
		byte distance = 0;
		if (effect.getProcessType() == ProcessType.COMMON_ATK.getCode()) {
			distance = getDistance(caster, targets.get(0), context);
		}
		SkillReport skillRpt = new SkillReport(caster.getFighterId(), effect.getSkillId(), distance);
		
		boolean result = false;
		for (Fighter target : targets) {
			TargetReport report = new TargetReport(target.getFighterId());
			
				
				/**第四步: 将效果应用到目标身上 */
				boolean result1 = castSkill(caster, target, report, effect, context);
				result = result || result1;
				
			
			//由于某些原因导致技能无法释放到该目标身上(比如一个伤害技能打到一个死人身上),则该数据包无效
			//或者该数据包不需要下发,则也把valid设置为false,比如一个战斗结束时加经验的技能，或者是一个虚拟出来的技能(比如Parser1240)
			if (report.isValid()) {
				skillRpt.targets.add(report);
			}
			if (context.tempTargetReports.size() > 0) {
				skillRpt.targets.addAll(context.tempTargetReports);
			}
			context.tempTargetReports.clear();
			
		}
		
//		addSkillReport(effect, skillRpt, context);
		
		return result;
	}
	
	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
			report.valid =false;
			return false;
		}
//		List<Integer> list = StringUtils.delimiterString2IntList(effect.getEffectExpr(), Splitable.ELEMENT_SPLIT);
//		if (list.contains(context.lastEffectId) == false) {
//			return false;
//		}
//		context.fightRecorder.recordImmunityAction(new ImmunityAction(target.getFighterId()));
		FighterBuffer buffer = newBuffer(context.generateBufferId(), caster, target, AttackerAttributeKey.IMMUNITY, effect, 1, false, BufferType.STATUS_BUFFER);
		target.addBuffer(buffer);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]增加了免疫效果", caster.getName(), effect.getSkillName(), target.getName());
		}
		return true;
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1181;
	}
}
