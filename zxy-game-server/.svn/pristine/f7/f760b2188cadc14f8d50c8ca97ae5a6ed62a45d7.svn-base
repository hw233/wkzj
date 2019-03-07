package com.jtang.gameserver.module.app.model.extension.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
import com.jtang.gameserver.module.app.type.EffectId;

@AppVO
public class RuleConfigVO18 implements BaseRuleConfigVO {

	/**
	 * 登陆奖励
	 */
	public Map<Integer,List<RewardObject>> reward = new HashMap<>();

	@Override
	public EffectId getEffectId() {
		return EffectId.EFFECT_ID_18;
	}

	@Override
	public void init(String record) {
		List<String[]> list = StringUtils.delimiterString2Array(record);
		for(String[] str : list){
			int day = Integer.valueOf(str[0]);
			List<RewardObject> rewardList = new ArrayList<>();
			List<String[]> strList = StringUtils.delimiterString2Array(str[1], Splitable.DELIMITER_ARGS, Splitable.BETWEEN_ITEMS);
			for(String[] rewardStr : strList){
				RewardObject rewardObject = new RewardObject();
				rewardObject.rewardType = RewardType.getType(Integer.valueOf(rewardStr[0]));
				rewardObject.id = Integer.valueOf(rewardStr[1]);
				rewardObject.num = Integer.valueOf(rewardStr[2]);
				rewardList.add(rewardObject);
			}
			reward.put(day, rewardList);
		}
		
	}

}
