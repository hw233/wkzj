package com.jtang.gameserver.module.user.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 角色动作行为请求类
 * @author 0x737263
 *
 */
public class ActorMontionRequest extends IoBufferSerializer {

	/**
	 * 行为主类型id
	 */
	public int mainType;
	
	/**
	 * 行为子类型id
	 */
	public int subType;
	
	/**
	 * 行为值
	 */
	public int value;
	
	public ActorMontionRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.mainType = readInt();
		this.subType = readInt();
		this.value = readInt();
	}

}
