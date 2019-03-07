package com.jtang.gameserver.module.cdkey.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jtang.core.lop.LopClient;
import com.jtang.core.lop.result.ListLopResult;
import com.jtang.gameserver.component.lop.request.CdkeyLOPRequest;
import com.jtang.gameserver.component.lop.response.CdkeyLOPResponse;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.cdkey.facade.CdkeyFacade;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class CdkeyFacadeImpl implements CdkeyFacade {
	@Autowired
	ActorFacade actorFacade;
	@Autowired
	GoodsFacade goodsFacade;
	@Autowired
	LopClient lopClient;

	@Override
	public ListLopResult<CdkeyLOPResponse> getPackage(String cdkey, long actorId) {
		Actor actor = actorFacade.getActor(actorId);
		if ( actor == null) {
			return new ListLopResult<CdkeyLOPResponse>();
		}
		CdkeyLOPRequest request = new CdkeyLOPRequest();
		request.cdKey = cdkey;
		request.channelId = actor.channelId;
		request.platformId = actor.platformType;
		request.uid = actor.uid;
		request.serverId = actor.serverId;
		request.actorId = actorId;

		String json = lopClient.execute(request);
		ListLopResult<CdkeyLOPResponse> result = JSON.parseObject(json, new TypeReference<ListLopResult<CdkeyLOPResponse>>() {
		});

		if (result.successed) {
			for (CdkeyLOPResponse rsp : result.item) {
				goodsFacade.addGoodsVO(actorId, GoodsAddType.CDKEY, rsp.goodsId, rsp.goodsNum);
			}
		}

		return result;
	}

}
