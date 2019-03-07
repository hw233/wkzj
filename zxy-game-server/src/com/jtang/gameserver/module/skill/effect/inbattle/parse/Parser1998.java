package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.constant.BattleRule;
import com.jtang.gameserver.module.battle.helper.FightProcessor;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.DeadAction;
import com.jtang.gameserver.module.battle.model.DisapperAction;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.SkillReport;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 文殊菩萨-合击
 *  x1 - x2 * x3
 * x1:友方所有人的攻击力之和
 * x2：被击者防御力
 * x3：系数
 * @author vinceruan
 *
 */
@Component
public class Parser1998 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1998;
	}

	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		if (targets.isEmpty()) {
			return false;
		}
		
		Fighter target = targets.get(0);
		
		if (target.isDead()) {
			return false;
		}
		
		processSkillBeforeBeAtked(target, context);
		
		//尝试闪避
		boolean im = tryImmunity(caster, target, effect, context);
		boolean dodge = FightProcessor.getInstance().tryDodge(target, context);
		if(im || dodge) {
			return true;
		}
		
		List<Byte> attackers = new ArrayList<>();
		attackers.add(caster.getFighterId());
		
		//计算同排所有友方仙人的攻击力之和
		int totalAtk = caster.getAtk();
		for (Fighter friend : context.getTeamListByCamp(caster.getCamp())) {
			if (friend.isDead()) {
				continue;
			}
			if (friend.getFighterId()==(caster.getFighterId())) {
				continue;
			}
			if (friend.getTile().getY() == caster.getTile().getY()) {
				totalAtk += friend.getAtk();
				attackers.add(friend.getFighterId());
			}
		}
		SkillReport fr = new SkillReport(attackers, effect.getSkillId());		
		TargetReport report = new TargetReport(target.getFighterId());
		fr.targets.add(report);
		
		int hurt = FormulaHelper.executeCeilInt(effect.getEffectExpr(), totalAtk, target.getDefense(), BattleRule.BATTLE_DEF_FACTOR);
		
		hurt = ensureValidHurt(hurt);
		
		//减血
		target.addHurt(Math.abs(hurt));
		
		//记录战报
		this.addAttributeChange(target, report, AttackerAttributeKey.HP);
		
		//记录受影响者(用于反击)
		context.addFighterBeAtcked(target);
		
		//统计阵营的总伤害值(用于对峙时胜败判定)
		context.addAtkHur(caster.getCamp(), hurt);
		if (target.isDead()) {
			DeadAction deadAction = new DeadAction(target.getFighterId());
			report.actions.add(deadAction);
			DisapperAction disapperAction = new DisapperAction(target.getFighterId());
			report.actions.add(disapperAction);
			deadAction.setDisapperAction(disapperAction);
		}
		
		addSkillReport(effect, fr, context);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] 释放技能 [{}], 对目标 [{}] 造成伤害值 [{}]", caster.getName(), effect.getSkillName(), target.getName(), hurt);
		}
		
		return true;
		
	}
}
