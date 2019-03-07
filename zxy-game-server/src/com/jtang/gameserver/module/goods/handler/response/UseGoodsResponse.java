package com.jtang.gameserver.module.goods.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
/**
 * 使用物品返回结果
 * @author ludd
 *
 */
public class UseGoodsResponse extends IoBufferSerializer {

	/**
	 * 使用结果数据
	 */
	public List<UseGoodsResult> resultList = new ArrayList<>();
	
	/**
	 * 使用的物品id
	 */
	public int goodsId;
	
	public UseGoodsResponse(List<UseGoodsResult> resultList, int goodsId) {
		super();
		if (resultList != null){
			this.resultList = resultList;
		}
		this.goodsId = goodsId;
	}


	@Override
	public void write() {
		this.writeShort((short)resultList.size());
		for (UseGoodsResult goodsResult : resultList) {
			this.writeBytes(goodsResult.getBytes());
		}
		this.writeInt(goodsId);
	}

}
