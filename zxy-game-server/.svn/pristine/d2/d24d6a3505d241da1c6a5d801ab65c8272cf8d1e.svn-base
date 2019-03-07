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
 * 提高自身的防御力(根据自身的基础防御力计算加成值)
 * @author vinceruan
 *
 */
@Component
public class Parser140 extends AbstractOutBattleEffectParser {

	@Override
	public List<BufferVO> parser(long actorId, int heroId, OutbattleEffectConfig effectConfig) {
		int baseDefense = heroFacade.getHeroBaseAttribute(actorId, heroId, HeroVOAttributeKey.DEFENSE);
		int addDefense = FormulaHelper.executeCeilInt(effectConfig.getEffectExpr(), baseDefense);
		
		List<BufferVO> list = new ArrayList<>();
		BufferVO buffer = new BufferVO(HeroVOAttributeKey.DEFENSE, addDefense);
		list.add(buffer);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("OutBattle,技能效果被激活且处理, heroId:[{}], effectId:[{}]", heroId, effectConfig.getEffectId());
		}
		
		return list;
	}

	@Override
	public int getParserId() {
		return OutbattleParserKey.Parser140;
	}

}
