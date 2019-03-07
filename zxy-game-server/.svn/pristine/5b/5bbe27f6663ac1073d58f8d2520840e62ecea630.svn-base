package com.jtang.gameserver.admin.facade.impl;

import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ACTOR_NOT_EXIST;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ADD_GOODS_ERROR;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.GOODS_CONFIG_NOT_EXISTS;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.admin.facade.GoodsMaintianFacade;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;
import com.jtang.gameserver.dataconfig.service.GoodsService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class GoodsMaintianFacadeImpl implements GoodsMaintianFacade {

	@Autowired
	ActorFacade actorFacade;

	@Autowired
	GoodsFacade goodsFacade;

	@Override
	public Result addGoods(long actorId, int goodsId, int num) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		TResult<Long> result = goodsFacade.addGoodsVO(actorId, GoodsAddType.ADMIN_ADD, goodsId, num);
		if (result.isOk()) {
			return Result.valueOf();
		}
		return Result.valueOf(ADD_GOODS_ERROR);
	}

	@Override
	public Result deleteGoods(long actorId, int goodsId, int num) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		GoodsConfig goodsConfig = GoodsService.get(goodsId);
		if (goodsConfig == null) {
			return Result.valueOf(GOODS_CONFIG_NOT_EXISTS);
		}
		Result result = goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.ADMIN, goodsId, num);
		return result;
	}


	@Override
	public Result addAllGoods(long actorId) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		Collection<GoodsConfig> collection = GoodsService.getAllConfigList();
		for (GoodsConfig goodsConfig : collection) {
			TResult<Long> result = goodsFacade.addGoodsVO(actorId, GoodsAddType.ADMIN_ADD, goodsConfig.getGoodsId(), 1);
			if (result.isFail()) {
				continue;
//				return Result.valueOf(ADD_GOODS_ERROR);
			}
		}
		return Result.valueOf();
	}
}
