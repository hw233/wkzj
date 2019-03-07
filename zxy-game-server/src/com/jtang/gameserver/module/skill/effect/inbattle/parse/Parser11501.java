package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.DeadAction;
import com.jtang.gameserver.module.battle.model.DisapperAction;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.SkillReport;
import com.jtang.gameserver.module.skill.model.TargetReport;
import com.jtang.gameserver.module.skill.type.ProcessType;

/**
 * 提高目标的攻击力(根据施法者的攻击力计算加成值)
 * @author ludd
 *
 */
@Component
public class Parser11501 extends AbstractInBattleEffectParser {

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
		int atk = caster.getAtk();
		for (Fighter target : targets) {
			TargetReport report = new TargetReport(target.getFighterId());
			
				
				/**第四步: 将效果应用到目标身上 */
			boolean result1 = addAtkComputedByOneArg(caster, target, report, atk, effect, context);
			
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
			if (result1 && target.isDead()) {
				DeadAction deadAction = new DeadAction(target.getFighterId());
				report.actions.add(deadAction);
				DisapperAction disapperAction = new DisapperAction(target.getFighterId());
				report.actions.add(disapperAction);
				deadAction.setDisapperAction(disapperAction);
			}
			
		}
		
		addSkillReport(effect, skillRpt, context);
		return result;
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser11501;
	}
}
