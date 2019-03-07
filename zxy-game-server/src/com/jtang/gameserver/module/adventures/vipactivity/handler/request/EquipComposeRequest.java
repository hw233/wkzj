package com.jtang.gameserver.module.adventures.vipactivity.handler.request;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 炼器宗师请求参数
 * @author 0x737263
 *
 */
public class EquipComposeRequest extends IoBufferSerializer {
	
	/**
	 * 详见{@code EquipType} 1.装备 2.防具 3.饰品
	 */
	public int equipType;
	
	/**
	 * 是否使用点券  0.不使用 1.使用 
	 */
	private int useTicket;
	
	/**
	 * 将要合成的装备uid
	 */
	public List<Long> uuidList;
	
	public EquipComposeRequest(byte[] bytes) {
		super(bytes);
	}
	
	public boolean isUseTicket() {
		return useTicket == 1 ? true : false;
	}

	@Override
	public void read() {
		equipType = readInt();
		useTicket = readInt();
		uuidList = readLongList();
	}
	


}
