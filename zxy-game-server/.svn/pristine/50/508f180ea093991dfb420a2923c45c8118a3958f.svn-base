package com.jtang.gameserver.module.adventures.favor.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.favor.model.FavorVO;
/**
 * 奇遇信息的Response；
 *
 * @author pengzy
 *
 */
public class FavorResponse extends IoBufferSerializer{

	/**
	 * 奇遇列表
	 */
	private FavorVO favorVO;
	
	public FavorResponse(FavorVO favorVO){
		this.favorVO = favorVO;
	}
	
	@Override
	public void write() {
		this.writeBytes(this.favorVO.getBytes());
	}
}
