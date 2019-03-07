package com.jtang.gameserver.module.hero.handler.response;

import java.util.List;

import com.jiatang.common.model.BufferVO;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.hero.model.HeroBufferVO;

/**
 * 更新一个或多个仙人身上的buffer
 * @author vinceruan
 *
 */
public class UpdateHerosBuffResponse extends IoBufferSerializer {
	
	List<HeroBufferVO> heroBuffs;
	
	@Override
	public void write() {
		writeShort((short)heroBuffs.size());
		for (HeroBufferVO hb : heroBuffs) {
			writeHeroBuff(hb);
		}
	}

	private void writeHeroBuff(HeroBufferVO hb) {
		writeInt(hb.heroId);
		writeShort((short)hb.buffList.size());
		for (BufferVO bf : hb.buffList) {
			writeByte(bf.key.getCode());
			writeInt(bf.addVal);
		}
	}
	
	public UpdateHerosBuffResponse(List<HeroBufferVO> heroBuffs) {
		this.heroBuffs = heroBuffs;
	}

}
