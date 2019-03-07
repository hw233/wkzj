package com.jtang.gameserver.module.delve.handler.request;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

public class OneKeyDelveRequest extends IoBufferSerializer {

	/**
	 * 仙人id
	 */
	public int heroId;
	
	/**
	 * 潜修次数
	 */
	public int type;
	
	/**
	 * 选择属性
	 * 1.攻
	 * 2.防
	 * 3.血
	 */
	public List<Integer> attribute;
	
	public OneKeyDelveRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.heroId = readInt();
		this.type = readInt();
		attribute = readIntegerList();
	}
	
}
