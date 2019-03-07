package com.jtang.gameserver.module.equip.handler.request;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

public class EquipUpRequest extends IoBufferSerializer {

	/**
	 * 装备列表
	 */
	public List<Long> equipList;
	
	public EquipUpRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.equipList = readLongList();
	}
}
