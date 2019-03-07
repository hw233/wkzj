package com.jtang.gameserver.module.demon.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.demon.type.DemonCamp;

/**
 * 请求加入阵营
 * @author ludd
 *
 */
public class PlayerJoinRequest extends IoBufferSerializer {
	/**
	 * 阵营选择
	 */
	public DemonCamp camp;
	
	public PlayerJoinRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		camp = DemonCamp.getByCode(this.readInt());
	}

}
