package com.jtang.gameserver.module.goods.handler.response;

import java.util.Collection;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.goods.model.GoodsVO;

/**
 * 物品列表响应对象
 * @author 0x737263
 *
 */
public class GoodsListResponse extends IoBufferSerializer {

	Collection<GoodsVO> list;

	@Override
	public void write() {
		writeShort((short)list.size());
		for(GoodsVO vo : list) {
			writeBytes(vo.getBytes());
		}
	}
	
	public static GoodsListResponse valueOf(Collection<GoodsVO> list) {
		GoodsListResponse response = new GoodsListResponse();
		response.list = list;
		return response;
	}
	
}
