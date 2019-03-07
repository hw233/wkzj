package com.jtang.gameserver.module.goods.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.goods.model.GoodsVO;

/**
 * 物品属性更新响应类
 * @author 0x737263
 *
 */
public class GoodsAttributeResponse extends IoBufferSerializer {

	/**
	 * 注：num为0是 则表示该物品为删除
	 */
	public GoodsVO goodsVo;
	
	
	public GoodsAttributeResponse(GoodsVO goodsVo) {
		this.goodsVo = goodsVo;
	}

	@Override
	public void write() {
		writeBytes(goodsVo.getBytes());
	}
}
