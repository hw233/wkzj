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
 * 提高目标的生命上限(按照基础生命上限值计算加成值)
 * @author vinceruan
 *
 */
@Component
public class Parser110 extends AbstractOutBattleEffectParser {

	@Override
	public List<BufferVO> parser(long actorId, int heroId, OutbattleEffectConfig effectConfig) {
		int hpMaxBase = heroFacade.getHeroBaseAttribute(actorId, heroId, HeroVOAttributeKey.HP);
		int addHpMax = FormulaHelper.executeCeilInt(effectConfig.getEffectExpr(), hpMaxBase);
		
		List<BufferVO> list = new ArrayList<>();
		//满血
		list.add(new BufferVO(HeroVOAttributeKey.HP, addHpMax));
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("OutBattle,技能效果被激活且处理, heroId:[{}], effectId:[{}]", heroId, effectConfig.getEffectId());
		}
		
		return list;
	}

	@Override
	public int getParserId() {
		return OutbattleParserKey.Parser110;
	}

}
