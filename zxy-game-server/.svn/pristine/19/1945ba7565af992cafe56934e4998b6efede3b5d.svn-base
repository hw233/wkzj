package com.jtang.gameserver.module.enhanced.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 强化装备的请求
 * 
 * @author pengzy
 * 
 */
public class EquipUpgradeRequest extends IoBufferSerializer {

	/**
	 * 需强化的装备唯一Id
	 */
	public long equipUuid;
	
	/**
	 * 强化次数
	 */
	public int upgradeNum;

	public EquipUpgradeRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		equipUuid = readLong();
		upgradeNum = readInt();
	}

}
