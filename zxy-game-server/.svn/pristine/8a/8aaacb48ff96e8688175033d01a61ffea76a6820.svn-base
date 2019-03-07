package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.ReviveAction;
import com.jtang.gameserver.module.battle.model.TeleportAction;
import com.jtang.gameserver.module.battle.model.Tile;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.SkillReport;
import com.jtang.gameserver.module.skill.model.TargetReport;
import com.jtang.gameserver.module.skill.type.ProcessType;
/**
 * 有概率复活一个已经死亡的仙人到随机的空位
 * @author ludd
 *
 */
@Component
public class Parser12002 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser12002;
	}
	
	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		if (caster.isDead()) {
			return false;
		}
		Fighter reviveTarget = getTarget(targets);
		if (reviveTarget == null) {
			return false;
		}
		
		String[] strs = StringUtils.split(effect.getEffectExpr(), Splitable.ELEMENT_SPLIT);
		AttackerAttributeKey key = AttackerAttributeKey.getByCode(Byte.valueOf(strs[0]));
		int hp = 0;
		if (key.equals(AttackerAttributeKey.HP_MAX)) {
			hp = FormulaHelper.executeCeilInt(strs[1], caster.getHpMax());
		} else if (key.equals(AttackerAttributeKey.ATK)) {
			hp = FormulaHelper.executeCeilInt(strs[1], caster.getAtk());
		} else if (key.equals(AttackerAttributeKey.DEFENSE)) {
			hp = FormulaHelper.executeCeilInt(strs[1], caster.getDefense());
		} else {
			LOGGER.error(String.format("不支持的属性id:[%s]", key.getCode()));
			return false;
		}
		if (hp > reviveTarget.getHpMax()) {
			hp = reviveTarget.getHpMax();
		}
		reviveTarget.setHp(hp);
		TargetReport report = new TargetReport(reviveTarget.getFighterId());
		addAttributeChange(reviveTarget, report, AttackerAttributeKey.HP);
		report.actions.add(new ReviveAction(reviveTarget.getFighterId()));
		if (context.getBattleMap().isWalk(reviveTarget.getTile())) {
			context.fighterRevive(reviveTarget);
		} else {
			Tile tile = context.battleMap.getRandomRoadTile();
			if (tile == null) {
				return false;
			}
			context.fightRecorder.recordRevive(new ReviveAction(reviveTarget.getFighterId()));
			report.actions.add(TeleportAction.valueOf(reviveTarget.getFighterId(), tile));
			reviveTarget.setTile(tile);
			context.fighterRevive(reviveTarget);
			
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]进行了复活", caster.getName(), effect.getSkillName(), reviveTarget.getName());
		}
		
		byte distance = 0;
		if (context.getProcessType() == ProcessType.COMMON_ATK) {
			distance = getDistance(caster, reviveTarget, context);
		}
		SkillReport skillRt = new SkillReport(caster.getFighterId(), effect.getSkillId(), distance);
		skillRt.targets.add(report);
		addSkillReport(effect, skillRt, context);
		return true;
	}
	
	private Fighter getTarget(List<Fighter> friends) {
		List<Fighter> fighters = new ArrayList<>();
		for (Fighter fighter : friends) {
			if (fighter.isDead()) {
				fighters.add(fighter);
			}
		}
		if (fighters.isEmpty()) {
			return null;
		}
		int index = RandomUtils.nextIntIndex(fighters.size());
		return fighters.get(index);
	}

}
