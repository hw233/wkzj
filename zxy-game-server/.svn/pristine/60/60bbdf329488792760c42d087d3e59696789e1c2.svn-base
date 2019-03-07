package com.jtang.gameserver.module.goods.effect;

import static com.jiatang.common.GameStatusCodeConstant.ENERGY_FULL_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.USE_GOODS_ADD_ENERGY_FAIL;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;
import com.jtang.gameserver.dataconfig.service.GoodsService;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.goods.type.UseGoodsParserType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.module.user.type.EnergyAddType;
/**
 * 恢复精力
 * @author ludd
 *
 */
@Component
public class UseParser3 extends UseParser {

	@Override
	protected UseGoodsParserType getParserType() {
		return UseGoodsParserType.TYPE3;
	}

	@Override
	public TResult<List<UseGoodsResult>> handler(long actorId, int goodsId, int useNum, int useFlag) {
		boolean enough = checkGoodsEnough(actorId, goodsId, useNum);
		if (!enough){
			return TResult.valueOf(GOODS_NOT_ENOUGH);
		}
		if(actorFacade.isAddEnergy(actorId, EnergyAddType.USE_GOODS) == false){
			return TResult.valueOf(ENERGY_FULL_ERROR);
		}
		GoodsConfig goodsConfig = GoodsService.get(goodsId);
		int addValue = Integer.valueOf(goodsConfig.effectValue) * useNum;
		
		Result decreaseResult = goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.BASE, goodsId, useNum);
		if (decreaseResult.isFail()){
			return TResult.valueOf(decreaseResult.statusCode);
		}
		boolean result = actorFacade.addEnergy(actorId, EnergyAddType.USE_GOODS, addValue);
		if (result){
			return TResult.sucess(null);
		} else {
			return TResult.valueOf(USE_GOODS_ADD_ENERGY_FAIL);
		}
	}

}
