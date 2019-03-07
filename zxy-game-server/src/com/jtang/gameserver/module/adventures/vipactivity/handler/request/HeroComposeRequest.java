package com.jtang.gameserver.module.adventures.vipactivity.handler.request;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 仙人合成请求参数
 * @author 0x737263
 *
 */
public class HeroComposeRequest extends IoBufferSerializer {
	
	/**
	 * 是否使用点券  0.不使用 1.使用 
	 */
	private int useTicket;
	
	/**
	 * 将要合成的仙人id
	 */
	public List<Integer> heroIdList;
	
	public HeroComposeRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		useTicket = readInt();
		heroIdList = readIntegerList();
	}
	
	public boolean isUseTicket() {
		return useTicket == 1 ? true : false;
	}

}
