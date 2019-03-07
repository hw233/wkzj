package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.constant.BattleRule;
import com.jtang.gameserver.module.battle.helper.FightProcessor;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.PositionAction;
import com.jtang.gameserver.module.battle.model.Tile;
import com.jtang.gameserver.module.buffer.model.FighterBuffer;
import com.jtang.gameserver.module.buffer.type.BufferType;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 移位+伤害
 * @author vinceruan
 *
 */
@Component
public class Parser1232 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1232;
	}

	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
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
		
		String expr = effect.getEffectExpr();
		//移位
		int step = Integer.parseInt(expr.split(Splitable.ELEMENT_SPLIT)[0]);
		Tile tile = target.getCamp().jump(target.getTile(), step);
		boolean result = context.battleMap.move(target.getTile(), tile);
		if (result) {
			target.setTile(tile);
//			MoveAction move = MoveAction.valueOf(target.getFighterId(), tile);
//			report.actions.add(move);
			report.actions.add(PositionAction.valueOf(target.getFighterId(), tile));
//			AttributeChange ch = AttributeChange.valueOf(AttackerAttributeKey.POSITION, tile);
//			report.addAttrChange(ch);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("[{}]释放技能[{}],[{}]移位到[{}]", caster.getName(), effect.getSkillName(), target.getName(), tile.getX()+","+tile.getY());
			}
		}		
		
		//计算伤害值
		int atk = caster.getAtk();
		int defense = target.getDefense();
		int hurt = FormulaHelper.executeCeilInt(expr.split(Splitable.ELEMENT_SPLIT)[1], atk, defense, BattleRule.BATTLE_DEF_FACTOR);
		hurt = ensureValidHurt(hurt);
		
		//添加持续性伤害
		FighterBuffer buffer = new FighterBuffer(context.generateBufferId(), hurt, AttackerAttributeKey.HP, caster, target, effect, true, BufferType.ATTR_BUFFER){

			@Override
			public boolean heartBeatInternal() {
				this.getOwner().addHurt(this.getAddVal());
				return true;
			}
		};
		this.addBuffer(buffer, target, effect, context, report); 
		
		return true;
	}
}
