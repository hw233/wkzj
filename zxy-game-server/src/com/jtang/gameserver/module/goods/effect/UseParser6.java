package com.jtang.gameserver.module.goods.effect;

import static com.jiatang.common.GameStatusCodeConstant.GOODS_NOT_ENOUGH;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;
import com.jtang.gameserver.dataconfig.service.GoodsService;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.goods.type.UseGoodsParserType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.module.user.type.GoldAddType;
/**
 * 金币包使用
 * @author ludd
 *
 */
@Component
public class UseParser6 extends UseParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(UseParser6.class);
	@Override
	protected UseGoodsParserType getParserType() {
		return UseGoodsParserType.TYPE6;
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
		Result decreaseResult = goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.CURRENCY, goodsId, useNum);
		if (decreaseResult.isFail()){
			return TResult.valueOf(decreaseResult.statusCode);
		}
		GoodsConfig goodsConfig = GoodsService.get(goodsId);
		int addValue = Integer.valueOf(goodsConfig.effectValue) * useNum;
		boolean addResult = actorFacade.addGold(actorId, GoldAddType.USEGOODS, addValue);
		if (addResult == false){
			LOGGER.error(String.format("添加金币失败,角色id:[%s]", actorId));
		}
		return TResult.sucess(null);
	}

}
