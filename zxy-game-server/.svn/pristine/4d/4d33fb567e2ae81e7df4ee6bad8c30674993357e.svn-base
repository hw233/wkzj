package com.jtang.gameserver.module.app.model.extension.rule;

import java.util.List;

import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.Splitable;

@AppVO
public class RuleConfigVO14 implements BaseRuleConfigVO {

	/**
	 * 类型
	 */
	public int type;
	
	/**
	 * 物品id
	 */
	public int goodsId;
	
	/**
	 * 数量
	 */
	public int num;
	
	/**
	 * 可购买次数
	 */
	public int buyNum;
	
	/**
	 * 消耗精力次数
	 */
	public int costEnergyNum;
	
	/**
	 * 消耗精力点
	 */
	public int costEnergy;
	
	/**
	 * 消耗点券点
	 */
	public int costTicket;
	
	@Override
	public EffectId getEffectId() {
		return EffectId.EFFECT_ID_14;
	}

	@Override
	public void init(String record) {
		List<Integer> list = StringUtils.delimiterString2IntList(record, Splitable.ATTRIBUTE_SPLIT);
		this.type = list.get(0);
		this.goodsId = list.get(1);
		this.num = list.get(2);
		this.buyNum = list.get(3);
		this.costEnergyNum = list.get(4);
		this.costEnergy = list.get(5);
		this.costTicket = list.get(6);
	}

}
