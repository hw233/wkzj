package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
/**
 * 射程范围内躲在敌人身后的敌人只受递减伤害
 * @author ludd
 *
 */
@Component
public class Parser2002 extends Parser2001 {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser2002;
	}

	/**
	 * 前方是否有2个人
	 */
	@Override
	protected boolean isAhead(Fighter target, Context context) {
		int aheadNum = getAheadFighter(target, context, true).size();
		if (aheadNum >= 2){
			return true;
		}
		return false;
	}
	

	
	

}
