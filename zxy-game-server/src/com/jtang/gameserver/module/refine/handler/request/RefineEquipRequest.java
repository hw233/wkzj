package com.jtang.gameserver.module.refine.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 精炼装备请求
 * 服务器回复信息为{@code RefineEquipResponse}
 * @author liujian
 *
 */
public class RefineEquipRequest extends IoBufferSerializer {

	/**
	 * 装备uuid
	 */
	public long uuid;
	
	/**
	 * 精炼类型
	 */
	public int refineType;
	
	/**
	 * 精炼次数
	 */
	public int refineNum;
	
	public RefineEquipRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		uuid = this.readLong();
		refineType = this.readByte();
		refineNum = this.readInt();
	}

}
