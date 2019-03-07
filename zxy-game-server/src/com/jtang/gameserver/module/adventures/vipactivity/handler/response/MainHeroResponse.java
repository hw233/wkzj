package com.jtang.gameserver.module.adventures.vipactivity.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 主力仙人的回复
 * @author pengzy
 *
 */
public class MainHeroResponse extends IoBufferSerializer {
	
	/**
	 * 主力仙人id
	 */
	private int heroId;
	
	/**
	 * 属性类型
	 * {@code HeroVOAttributeKey}}
	 */
	private byte attributeType;
	
	/**
	 * 属性值千分比
	 * 
	 */
	private int attributeValuePercent;
	
	
	public MainHeroResponse(int heroId, byte attributeType, int attributeValuePercent) {
		this.heroId = heroId;
		this.attributeType = attributeType;
		this.attributeValuePercent = attributeValuePercent;
	}
	
	@Override
	public void write() {
		writeInt(heroId);
		writeByte(attributeType);
		writeInt(attributeValuePercent);
	}

}
