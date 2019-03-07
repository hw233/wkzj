package com.jtang.gameserver.module.goods.effect;

import static com.jiatang.common.GameStatusCodeConstant.GOODS_NOT_ENOUGH;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;
import com.jtang.gameserver.dataconfig.service.GoodsService;
import com.jtang.gameserver.module.demon.facade.DemonFacade;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.goods.type.UseGoodsParserType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;

@Component
public class UseParser8 extends UseParser {

	@Autowired
	private DemonFacade demonFacade;
	@Override
	protected UseGoodsParserType getParserType() {
		return UseGoodsParserType.TYPE8;
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
		int addValue = Integer.valueOf(goodsConfig.effectValue) * useNum;
		Result decreaseResult = goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.BASE, goodsId, useNum);
		if (decreaseResult.isFail()){
			return TResult.valueOf(decreaseResult.statusCode);
		}
		demonFacade.addDemonScore(actorId, addValue);
		return TResult.sucess(null);
	}

}
