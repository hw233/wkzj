package com.jtang.gameserver.module.goods.helper;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.goods.handler.GoodsCmd;
import com.jtang.gameserver.module.goods.handler.response.GoodsAttributeResponse;
import com.jtang.gameserver.module.goods.model.GoodsVO;
import com.jtang.gameserver.server.broadcast.Broadcast;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class GoodsPushHelper {
	@Autowired
	Broadcast broadcast;
	
	@Autowired
	PlayerSession playerSession;
	
	private static ObjectReference<GoodsPushHelper> REF = new ObjectReference<GoodsPushHelper>();

	@PostConstruct
	protected void init() {
		REF.set(this);
	}
	
	private static GoodsPushHelper getInstance() {
		return REF.get();
	}
	
	/**
	 * 推送物品数量变更
	 * @param actorId
	 * @param uuid
	 * @param goodsNum
	 */
	public static void pushGoodsAttribute(long actorId, GoodsVO goodsVo) {
		GoodsAttributeResponse packet = new GoodsAttributeResponse(goodsVo);
		Response response = Response.valueOf(ModuleName.GOODS, GoodsCmd.PUSH_GOODS_ATTRUBITE, packet.getBytes());
		getInstance().playerSession.push(actorId, response);
	}
	public static void pushGoods(long actorId, List<GoodsVO> goodsVos) {
		for (GoodsVO goodsVO : goodsVos) {
			pushGoodsAttribute(actorId, goodsVO);
		}
	}
	
	
}
