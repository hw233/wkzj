package com.jtang.gameserver.module.goods.effect;

import static com.jiatang.common.GameStatusCodeConstant.*;

import java.util.List;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.goods.type.UseGoodsParserType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
/**
 * 无解析器，使用直接返回错误
 * @author ludd
 *
 */
public class UseParser0 extends UseParser {

	@Override
	protected UseGoodsParserType getParserType() {
		return UseGoodsParserType.NONE;
	}

	@Override
	public TResult<List<UseGoodsResult>> handler(long actorId, int goodsId, int useNum, int useFlag) {
		return TResult.valueOf(USE_GOODS_NOT_SUPPORT);
	}

}
