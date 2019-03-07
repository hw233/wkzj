package com.jtang.gameserver.module.extapp.welkin.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.extapp.welkin.module.WelkinRankVO;

public class WelkinRankResponse extends IoBufferSerializer {

	/**
	 * 排行榜
	 */
	public List<WelkinRankVO> list = new ArrayList<>();
	
	@Override
	public void write() {
		writeShort((short) list.size());
		for(WelkinRankVO rankVO:list){
			writeBytes(rankVO.getBytes());
		}
	}
}
