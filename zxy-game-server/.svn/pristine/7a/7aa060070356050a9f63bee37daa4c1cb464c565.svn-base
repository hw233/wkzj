package com.jtang.gameserver.module.buffer.model;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.type.BufferType;
/**
 * 防御改变buffer
 * @author ludd
 *
 */
public class DeffendsBuffer extends FighterBuffer {

	public DeffendsBuffer(int id, int addVal, Fighter caster, Fighter owner, InbattleEffectConfig effect) {
		super(id, addVal, AttackerAttributeKey.DEFENSE, caster, owner, effect, true, BufferType.ATTR_BUFFER);
	}
	
	@Override
	protected boolean heartBeatInternal() {
		//改变防御
//		getOwner().decreasDeffends(-getAddVal());				
		return true;
	}

}
