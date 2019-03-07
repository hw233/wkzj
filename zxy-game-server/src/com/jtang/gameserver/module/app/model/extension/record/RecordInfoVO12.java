package com.jtang.gameserver.module.app.model.extension.record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.app.type.AppKey;

@AppVO
public class RecordInfoVO12 implements BaseRecordInfoVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8380598970476376373L;

	/**
	 * 充值金额
	 */
	public int rechargeMoney;

	/**
	 * 是否发放
	 */
	public int isSend;

	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_12;
	}

	@Override
	public String parse2String() {
		StringBuffer sb = new StringBuffer();
		sb.append(rechargeMoney);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(isSend);
		return sb.toString();
	}

	@Override
	public BaseRecordInfoVO valueOf(String record) {
		RecordInfoVO12 recordInfoVO1012 = new RecordInfoVO12();
		List<Integer> list = StringUtils.delimiterString2IntList(record, Splitable.ATTRIBUTE_SPLIT);
		if (list.isEmpty()) {
			recordInfoVO1012.rechargeMoney = 0;
			recordInfoVO1012.isSend = 0;
		} else {
			recordInfoVO1012.rechargeMoney = list.get(0);
			recordInfoVO1012.isSend = list.get(1);
		}
		return recordInfoVO1012;
	}

	@Override
	public Map<AppKey, Object> getRecordInfoMaps() {
		Map<AppKey, Object> map = new HashMap<>();
		map.put(AppKey.RECHARGE_MONEY, rechargeMoney);
		map.put(AppKey.IS_GET, isSend);
		return map;
	}
	
	public boolean isGet(){
		return isSend == 0;
	}

}
