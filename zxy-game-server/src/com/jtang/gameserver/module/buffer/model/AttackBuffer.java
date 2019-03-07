package com.jtang.gameserver.module.buffer.model;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.type.BufferType;
/**
 * 攻击改变buffer
 * @author ludd
 *
 */
public class AttackBuffer extends FighterBuffer {

	public AttackBuffer(int id, int addVal, Fighter caster, Fighter owner, InbattleEffectConfig effect) {
		super(id, addVal, AttackerAttributeKey.ATK, caster, owner, effect, true, BufferType.ATTR_BUFFER);
	}
	
	@Override
	protected boolean heartBeatInternal() {
//		getOwner().decreasAttack(-getAddVal());				
		return true;
	}

}
