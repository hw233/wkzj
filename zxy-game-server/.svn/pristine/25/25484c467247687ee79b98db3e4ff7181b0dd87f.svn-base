package com.jtang.gameserver.module.goods.effect;

import static com.jiatang.common.GameStatusCodeConstant.GOODS_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.USE_GOODS_ADD_VIT_FAIL;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;
import com.jtang.gameserver.dataconfig.service.GoodsService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.goods.type.UseGoodsParserType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.module.user.type.VITAddType;
/**
 * 增加上限并补满活力
 * @author ludd
 *
 */
@Component
public class UseParser13 extends UseParser {

	@Override
	protected UseGoodsParserType getParserType() {
		return UseGoodsParserType.TYPE13;
	}

	@Override
	public TResult<List<UseGoodsResult>> handler(long actorId, int goodsId, int useNum, int useFlag) {
		boolean enough = checkGoodsEnough(actorId, goodsId, useNum);
		if (!enough){
			return TResult.valueOf(GOODS_NOT_ENOUGH);
		}
		int goodsCount = goodsFacade.getCount(actorId, goodsId);
		if(goodsCount < useNum){
			if(goodsCount == 0){
				return TResult.valueOf(GOODS_NOT_ENOUGH);
			}else{
				useNum = goodsCount;
			}
		}
		GoodsConfig goodsConfig = GoodsService.get(goodsId);
		boolean result = false;
		int effect = Integer.valueOf(goodsConfig.effectValue);
		Result decreaseResult = goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.BASE, goodsId, useNum);
		if (decreaseResult.isFail()) {
			return TResult.valueOf(decreaseResult.statusCode);
		}
		int addNum = Integer.valueOf(effect) * useNum;
		Actor actor = actorFacade.getActor(actorId);
		boolean result1 = actorFacade.addVITLimit(actorId, addNum);
		boolean result2 = actorFacade.addVIT(actorId, VITAddType.USE_GOODS,actor.maxVit);
		result = result1 && result2;
		if (result) {
			return TResult.sucess(null);
		} else {
			return TResult.valueOf(USE_GOODS_ADD_VIT_FAIL);
		}

	}

}
