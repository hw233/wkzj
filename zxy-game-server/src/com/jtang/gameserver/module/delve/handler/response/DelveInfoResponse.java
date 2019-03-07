package com.jtang.gameserver.module.delve.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.delve.model.DelveVO;
/**
 * 返回潜修室数据
 * @author ludd
 *
 */
public class DelveInfoResponse extends IoBufferSerializer {
	/**
	 * 潜修室数据
	 */
	private DelveVO delveVO;
	
	public DelveInfoResponse(DelveVO delveVO) {
		super();
		this.delveVO = delveVO;
	}

	@Override
	public void write() {
		this.writeBytes(delveVO.getBytes());
	}

}
