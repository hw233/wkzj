package com.jtang.gameserver.module.buffer.model;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.type.BufferType;

public class HpMaxBuffer extends FighterBuffer {

	public HpMaxBuffer(int id, int addVal, Fighter caster, Fighter owner, InbattleEffectConfig effect) {
		super(id, addVal, AttackerAttributeKey.HP_MAX, caster, owner, effect, true, BufferType.ATTR_BUFFER);
	}
	
	@Override
	protected boolean heartBeatInternal() {
//		getOwner().decreasHpMax(-getAddVal());
		return super.heartBeatInternal();
	}

}
