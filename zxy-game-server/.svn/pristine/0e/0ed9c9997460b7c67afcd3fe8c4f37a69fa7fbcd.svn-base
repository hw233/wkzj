package com.jtang.gameserver.module.delve.model;

import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 潜修结果
 * @author ludd
 *
 */
public class DoDelveResult extends IoBufferSerializer{
	
	/**
	 * 属性ID
	 */
	public HeroVOAttributeKey heroVOAttributeKey;
	
	/**
	 * 属性值
	 */
	public int value;
	

	public DoDelveResult(HeroVOAttributeKey heroVOAttributeKey, int value) {
		super();
		this.heroVOAttributeKey = heroVOAttributeKey;
		this.value = value;
	}


	@Override
	public void write() {
		this.writeByte(heroVOAttributeKey.getCode());
		this.writeInt(value);
	}
}
