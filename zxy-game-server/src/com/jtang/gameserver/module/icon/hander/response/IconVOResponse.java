package com.jtang.gameserver.module.icon.hander.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.icon.model.IconVO;

public class IconVOResponse extends IoBufferSerializer {

	/**
	 * 头像和边框
	 */
	public IconVO iconVO;
	
	public IconVOResponse(IconVO iconVO){
		this.iconVO = iconVO;
	}
	
	@Override
	public void write() {
		writeBytes(iconVO.getBytes());
	}
}
