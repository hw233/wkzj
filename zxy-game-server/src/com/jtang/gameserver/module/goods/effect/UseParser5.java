package com.jtang.gameserver.module.goods.effect;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.goods.type.UseGoodsParserType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.module.recruit.facade.RecruitFacade;
/**
 * 补充聚仙次数
 * @author ludd
 *
 */
@Component
public class UseParser5 extends UseParser {

	@Autowired
	private RecruitFacade recruitFacade;
	@Override
	protected UseGoodsParserType getParserType() {
		return UseGoodsParserType.TYPE5;
	}

	@Override
	public TResult<List<UseGoodsResult>> handler(long actorId, int goodsId, int useNum, int useFlag) {

//		boolean enough = checkGoodsEnough(actorId, goodsId, useNum);
//		if (!enough) {
//			return TResult.valueOf(GOODS_NOT_ENOUGH);
//		}
//
//		GoodsConfig goodsConfig = GoodsService.get(goodsId);
//		boolean result = recruitFacade.addChange(actorId, Integer.valueOf(goodsConfig.effectValue));
//		if (result == false) {
//			return TResult.valueOf(USE_GOODS_RECRUIT_FULL);
//		}
//
//		Result decreaseResult = goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.BASE, goodsId, useNum);
//		if (decreaseResult.isFail()) {
//			return TResult.valueOf(decreaseResult.statusCode);
//		}

		return TResult.sucess(null);
	}

}
