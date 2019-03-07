package com.jtang.gameserver.module.adventures.favor.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.favor.model.FavorVO;
/**
 * 使用特权返回结果
 * @author ludd
 *
 */
public class PrivilegeUseResponse extends IoBufferSerializer {

	/**
	 * 福神眷顾数据
	 */
	private FavorVO favorVO;
	public PrivilegeUseResponse(FavorVO favorVO) {
		this.favorVO = favorVO;
	}
	@Override
	public void write() {
		this.writeBytes(this.favorVO.getBytes());
	}

}
