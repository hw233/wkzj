package com.jtang.gameserver.module.app.model.extension.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.app.type.AppKey;
/**
 * 《限量仙人大放送》扩展记录
 * 格式:可领次数_已领次数_累计充值金额
 * @author ludd
 *
 */
@AppVO
public class RecordInfoVO1 implements BaseRecordInfoVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -60468041234838784L;
	/**
	 * 可领次数
	 */
	public int rewardNum;
	/**
	 * 已领取次数
	 */
	public int hadRewardNum;
	/**
	 * 充值金额
	 */
	public int rechargeMoney;
	
	@Override
	public Map<AppKey, Object> getRecordInfoMaps() {
		Map<AppKey , Object> map = new HashMap<AppKey, Object>();
		map.put(AppKey.REWARD_NUM, this.rewardNum);
		map.put(AppKey.HAD_REWARD_NUM, this.hadRewardNum);
		map.put(AppKey.RECHARGE_MONEY, this.rechargeMoney);
		return map;
	}

	@Override
	public String parse2String() {
		List<Object> list = new ArrayList<>();
		list.add(rewardNum);
		list.add(hadRewardNum);
		list.add(rechargeMoney);
		return StringUtils.collection2SplitString(list, Splitable.ATTRIBUTE_SPLIT);
	}

	public boolean isReward() {
		return rewardNum > hadRewardNum;
	}


	@Override
	public BaseRecordInfoVO valueOf(String record) {
		RecordInfoVO1 vo = new RecordInfoVO1();
		if (StringUtils.isNotBlank(record)) {
			String[] strs = StringUtils.split(record, Splitable.ATTRIBUTE_SPLIT);
			strs = StringUtils.fillStringArray(strs, 3, "0");
			vo.rewardNum = Integer.valueOf(strs[0]);
			vo.hadRewardNum = Integer.valueOf(strs[1]);
			vo.rechargeMoney = Integer.valueOf(strs[2]);
		}
		return vo;
	}

	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_1;
	}
}
