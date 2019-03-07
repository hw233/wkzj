package com.jtang.gameserver.module.app.model.extension.rule;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
import com.jtang.gameserver.module.app.model.extension.rulevo.ExchangeGoods;
import com.jtang.gameserver.module.app.type.EffectId;
@AppVO
public class RuleConfigVO2 implements BaseRuleConfigVO {

	private List<ExchangeGoods> exchangeGoodsList = new ArrayList<>();
	@Override
	public EffectId getEffectId() {
		return EffectId.EFFECT_ID_2;
	}

	@Override
	public void init(String record) {
		List<String[]> list = StringUtils.delimiterString2Array(record);
		for (String[] strings : list) {
			ExchangeGoods exchangeGoods = new ExchangeGoods(strings);
			exchangeGoodsList.add(exchangeGoods);
		}
	}

	public ExchangeGoods get(int goodsId, int goodsNum) {
		for (ExchangeGoods exchangeGoods : exchangeGoodsList) {
			if (exchangeGoods.goodsId == goodsId && exchangeGoods.goodsNum == goodsNum) {
				return exchangeGoods;
			}
		}
		return null;
	}

}
