package com.jtang.gameserver.module.goods.helper;

import static com.jtang.core.protocol.StatusCode.DATA_VALUE_ERROR;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;
import com.jtang.gameserver.dataconfig.service.GoodsService;
import com.jtang.gameserver.module.goods.effect.UseParser;
import com.jtang.gameserver.module.goods.type.UseGoodsParserType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
/**
 * 使用物品帮助工具
 * @author ludd
 *
 */
public class UseGoodsHelper {
	private final static Logger LOGGER = LoggerFactory.getLogger(UseGoodsHelper.class);
	/**
	 * 处理物品使用
	 * @param actorId
	 * @param goodsId
	 * @param useNum
	 * @param useFlag
	 * @return
	 */
	public static TResult<List<UseGoodsResult>> processUseGoods(long actorId, int goodsId,int useNum, int useFlag){
		GoodsConfig goodsConfig = GoodsService.get(goodsId);
		UseGoodsParserType type = UseGoodsParserType.getUseGoodsParserType(goodsConfig.parserId);
		UseParser uGoodsEffectParser = UseParser.getParser(type);
		if (uGoodsEffectParser == null) {
			LOGGER.error(String.format("物品解析器不存在, type:[%s]", type.getType()));
			return TResult.valueOf(DATA_VALUE_ERROR);
		}
		return uGoodsEffectParser.handler(actorId, goodsId, useNum, useFlag);
	}
}
