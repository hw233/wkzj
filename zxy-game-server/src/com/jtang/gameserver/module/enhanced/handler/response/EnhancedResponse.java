package com.jtang.gameserver.module.enhanced.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 请求强化室的信息
 * @author pengzy
 *
 */
public class EnhancedResponse extends IoBufferSerializer {

	/**
	 * 当前强化室等级
	 */
	private int level;
	
	public EnhancedResponse(int level){
		this.level = level;
	}
	@Override
	public void write() {
		writeInt(level);
	}

}
