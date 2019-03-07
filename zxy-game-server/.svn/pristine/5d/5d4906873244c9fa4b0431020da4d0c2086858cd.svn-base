package com.jtang.gameserver.module.hero.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.hero.model.HeroUnit;

/**
 * 招募仙人响应
 * @author vinceruan
 *
 */
public class Soul2HeroResponse extends IoBufferSerializer {
	/**
	 * 仙人ID(招募到的仙人的详细信息另外通过HeroResponse推送, 魂魄的剩余数量也是通过HeroSoulResponse推送)
	 */
	int heroId;
	
	@Override
	public void write() {
		writeInt(heroId);
	}
	
	public Soul2HeroResponse(HeroUnit unit) {
		this.heroId = unit.id;
	}

}
