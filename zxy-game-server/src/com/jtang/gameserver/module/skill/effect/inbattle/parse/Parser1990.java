package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.constant.BattleRule;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.DeadAction;
import com.jtang.gameserver.module.battle.model.DisapperAction;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.Tile;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.SkillReport;
import com.jtang.gameserver.module.skill.model.TargetReport;
import com.jtang.gameserver.module.skill.type.ProcessType;

/**
 * 对同一列的多个敌人发到M次攻击(只有前一个敌人死亡时才会攻击下一个)
 * @author vinceruan
 *
 */
@Component
public class Parser1990 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1990;
	}

	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		if (targets.isEmpty()) {			
			return false;
		}
		
		//筛选未死亡的敌人
		Map<Tile, Fighter> map = new HashMap<Tile, Fighter>();
		for (Fighter f : targets) {
			if (f.isDead()) {
				continue;
			}
			map.put(f.getTile(), f);
		}
		
		if (map.isEmpty()) {
			return false;
		}
		
		//按前后排序
		Set<Tile> set = caster.getCamp().getEnermyCamp().sortByCol(map.keySet());
		Iterator<Tile> iter = set.iterator();
		Fighter target = null;
		
		//逐次发动攻击
		String exprs[] = effect.getEffectExpr().split(Splitable.ELEMENT_SPLIT);
		int times = Integer.valueOf(exprs[1]);
		int c = 0;
		byte distance = 0;
		if (context.getProcessType() == ProcessType.COMMON_ATK) {
			distance = getDistance(caster, target, context);
		}
		SkillReport fr = new SkillReport(caster.getFighterId(), effect.getSkillId(), distance);
		fr.targets.add(new TargetReport(caster.getFighterId()));
		addSkillReport(effect, fr, context);		
		while(++c <= times) {
			if (target == null || target.isDead()) {				
				while(iter.hasNext()) {
					target = map.get(iter.next());
					if (!target.isDead()) {
						break;
					}
				}
			}
			
			if (target == null || target.isDead()) {
				break;
			}
			byte  d = 0;
			if (context.getProcessType() == ProcessType.COMMON_ATK) {
				distance = getDistance(caster, target, context);
			}
			SkillReport fr1 = new SkillReport(caster.getFighterId(), Integer.valueOf(exprs[0]), d);
			TargetReport report = new TargetReport(target.getFighterId());
			fr1.targets.add(report);
			
			int atk = caster.getAtk();
			int defense = target.getDefense();
			
			//计算伤害值
			int hurt = FormulaHelper.executeCeilInt(exprs[2], atk, defense, BattleRule.BATTLE_DEF_FACTOR);
			
			addActualHurt(caster, target, report, effect, context, hurt);
			if (target.isDead()) {
				DeadAction deadAction = new DeadAction(target.getFighterId());
				report.actions.add(deadAction);
				DisapperAction disapperAction = new DisapperAction(target.getFighterId());
				report.actions.add(disapperAction);
				deadAction.setDisapperAction(disapperAction);
			}
			
			addSkillReport(effect, fr1, context);						
		}
		
		return true;
	}

}
