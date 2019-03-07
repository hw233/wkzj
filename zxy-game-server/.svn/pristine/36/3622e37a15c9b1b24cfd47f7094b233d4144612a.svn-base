package com.jtang.gameserver.module.chat.handler.response;

import java.util.List;

import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.module.icon.model.IconVO;

public class OpenBoxChatResponse extends ChatResponse {

	/**
	 * 打开宝箱id
	 */
	public int boxId;
	
	/**
	 * 获取内容
	 */
	public List<UseGoodsResult> results;
	
	public OpenBoxChatResponse(int msgType, String sendName, long actorId, int level, int vipLevel,int boxId,List<UseGoodsResult> results,IconVO iconVO) {
		super(msgType, sendName, actorId, level, vipLevel,iconVO);
		this.boxId = boxId;
		this.results = results;
	}
	
	@Override
	public void write() {
		super.write();
		writeInt(boxId);
		writeShort((short) results.size());
		for(UseGoodsResult goodsResutl:results){
			this.writeBytes(goodsResutl.getBytes());
		}
	}

}
