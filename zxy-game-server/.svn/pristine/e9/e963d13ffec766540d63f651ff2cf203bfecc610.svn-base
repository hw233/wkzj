package com.jtang.gameserver.module.skill.effect.outbattle.parse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.gameserver.dataconfig.model.OutbattleEffectConfig;
import com.jtang.gameserver.module.skill.effect.OutbattleParserKey;
import com.jtang.gameserver.module.skill.effect.outbattle.AbstractOutBattleEffectParser;

/**
 * 增加目标的攻击力(根据基础攻击力计算加成值)
 * @author vinceruan
 *
 */
@Component
public class Parser151 extends AbstractOutBattleEffectParser {

	@Override
	public List<BufferVO> parser(long actorId, int heroId, OutbattleEffectConfig effectConfig) {
		int atk = heroFacade.getHeroBaseAttribute(actorId, heroId, HeroVOAttributeKey.ATK);
		int addAtk = FormulaHelper.executeCeilInt(effectConfig.getEffectExpr(), atk);
		
		List<BufferVO> list = new ArrayList<>();
		BufferVO buffer = new BufferVO(HeroVOAttributeKey.ATK, addAtk);
		list.add(buffer);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("OutBattle,技能效果被激活且处理, heroId:[{}], effectId:[{}]", heroId, effectConfig.getEffectId());
		}
		
		return list;
	}

	@Override
	public int getParserId() {
		return OutbattleParserKey.Parser151;
	}

}
