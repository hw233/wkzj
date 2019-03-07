package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.constant.BattleRule;
import com.jtang.gameserver.module.battle.helper.FightProcessor;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.PositionAction;
import com.jtang.gameserver.module.battle.model.Tile;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * @author vinceruan
 *
 */
@Component
public class Parser1994 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1994;
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
		
		//将目标拖到身后
		if (!target.isDead()) {
			context.battleMap.jump(target.getTile(), tile);
			target.setTile(tile);
			report.actions.add(PositionAction.valueOf(target.getFighterId(), tile));
		}
		
		int atk = caster.getAtk();
		int defense = target.getDefense();
		
		//计算伤害值
		int hurt = FormulaHelper.executeCeilInt(effect.getEffectExpr(), atk, defense, BattleRule.BATTLE_DEF_FACTOR);
		
		if (hurt > 0) {//策划控制是否产生伤害
			
			target.addHurt(hurt);
			
			//记录战报
			this.addAttributeChange(target, report, AttackerAttributeKey.HP);
			
			//记录受影响者(用于反击)
			context.addFighterBeAtcked(target);
			
			//统计阵营的总伤害值(用于对峙时胜败判定)
			context.addAtkHur(caster.getCamp(), hurt);
		}		

		
		
		return true;
	}
}
