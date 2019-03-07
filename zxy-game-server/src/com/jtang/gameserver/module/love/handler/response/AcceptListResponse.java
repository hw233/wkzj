package com.jtang.gameserver.module.love.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.love.model.MarryAcceptVO;

public class AcceptListResponse extends IoBufferSerializer {
	/**
	 * 请求结婚列表
	 */
	private List<MarryAcceptVO> acceptList;

	public AcceptListResponse(List<MarryAcceptVO> acceptList) {
		super();
		this.acceptList = acceptList;
	}
	
	@Override
	public void write() {
		this.writeShort((short) this.acceptList.size());
		for (MarryAcceptVO acceptVO : acceptList) {
			this.writeBytes(acceptVO.getBytes());
		}
	}
}
