package com.jtang.gameserver.module.app.model.extension.rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
import com.jtang.gameserver.module.app.model.extension.rulevo.RankGoodsVO;
import com.jtang.gameserver.module.app.type.EffectId;

@AppVO
public class RuleConfigVO7 implements BaseRuleConfigVO  {

	/**
	 * 领取礼物列表
	 */
	public Map<Integer,RankGoodsVO> rewardMap = new HashMap<>();
	
	@Override
	public EffectId getEffectId() {
		return EffectId.EFFECT_ID_7;
	}

	@Override
	public void init(String record) {
		List<String[]> list = StringUtils.delimiterString2Array(record);
		for(String[] array:list){
			RankGoodsVO rankGoodsVO = new RankGoodsVO(array);
			rewardMap.put(rankGoodsVO.rank, rankGoodsVO);
		}
	}

}
