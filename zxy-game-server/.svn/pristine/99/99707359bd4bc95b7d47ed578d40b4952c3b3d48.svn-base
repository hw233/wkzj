package com.jtang.gameserver.module.app.model.extension.record;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.app.type.AppKey;

@AppVO
public class RecordInfoVO11 implements BaseRecordInfoVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8150284048869382920L;

	/**
	 * 充值金额
	 */
	public int rechargeMoney;

	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_11;
	}

	@Override
	public String parse2String() {
		return String.valueOf(rechargeMoney);
	}

	@Override
	public BaseRecordInfoVO valueOf(String record) {
		RecordInfoVO11 recordInfoVO1011 = new RecordInfoVO11();
		if(StringUtils.isNotBlank(record)){
			recordInfoVO1011.rechargeMoney = Integer.valueOf(record);
		}
		return recordInfoVO1011;
	}

	@Override
	public Map<AppKey, Object> getRecordInfoMaps() {
		Map<AppKey, Object> map = new HashMap<>();
		map.put(AppKey.RECHARGE_MONEY, rechargeMoney);
		return map;
	}

}
