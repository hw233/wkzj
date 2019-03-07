package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.ReviveAction;
import com.jtang.gameserver.module.battle.type.Camp;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 感化
 * @author vinceruan
 *
 */
@Component
public class Parser1995 extends AbstractInBattleEffectParser {

	@Override
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		Camp atkCamp = caster.getCamp();
		Camp defenseCamp = target.getCamp();
		if (atkCamp == defenseCamp) {
			report.valid = false;
			return false;
		}
		boolean isUseSkill = false;
		List<Fighter> defendTeam = context.getTeamListByCamp(defenseCamp);
		for (Fighter fighter : defendTeam) {
			if (!fighter.isDead()){
				isUseSkill = true;
				break;
			}
		}
		if (!isUseSkill){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("[{}]释放技能[{}],敌方目标[{}]已经全部阵亡，技能不触发！", caster.getName(), effect.getSkillName(), target.getName());
			}
			report.valid = false;
			return false;
		}
		Iterator<Fighter> it = context.getTeamListByCamp(defenseCamp).iterator();
		
		int hpValue = FormulaHelper.executeInt(effect.getEffectExpr(), target.getHpMax());
		target.setHp(hpValue);
		report.actions.add(new ReviveAction(target.getFighterId()));
		addAttributeChange(target, report, AttackerAttributeKey.HP);
		
		while(it.hasNext()) {
			if (it.next().getFighterId()== (target.getFighterId())) {
				target.setCamp(atkCamp);
				addAttributeChange(target, report, AttackerAttributeKey.CAMP);
				context.addFighter2Camp(target);
				it.remove();
				//恢复地图占位
				context.battleMap.addBarrier(target.getTile());
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("[{}]释放技能[{}],敌方目标[{}]已经成为友方", caster.getName(), effect.getSkillName(), target.getName());
				}
				return true;
			}
		}
		LOGGER.error("目标不存在,不能释放技能");
		return false;
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1995;
	}

}
