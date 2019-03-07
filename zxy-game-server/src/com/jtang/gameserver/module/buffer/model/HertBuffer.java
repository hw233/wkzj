package com.jtang.gameserver.module.buffer.model;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.type.BufferType;

/**
 * 伤害buffer
 * @author ludd
 *
 */
public class HertBuffer extends FighterBuffer {
	private Context context;
	public HertBuffer(int id, int addVal, AttackerAttributeKey attr, Fighter caster, Fighter owner, InbattleEffectConfig effect, Context context) {
		super(id, addVal, attr, caster, owner, effect, true, BufferType.ATTR_BUFFER);
		this.context = context;
		
	}
	@Override
	protected boolean heartBeatInternal() {
		//减血
		getOwner().addHurt(Math.abs(getAddVal()));				
		//统计阵营的总伤害值(用于对峙时胜败判定)
		context.addAtkHur(getCaster().getCamp(), getAddVal());
		return true;
	}

}
