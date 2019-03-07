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
public class RecordInfoVO16 implements BaseRecordInfoVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4050021414226131585L;

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
		return EffectId.EFFECT_ID_16;
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
		RecordInfoVO16 recordInfoVO1016 = new RecordInfoVO16();
		List<Integer> list = StringUtils.delimiterString2IntList(record, Splitable.ATTRIBUTE_SPLIT);
		if (list.isEmpty()) {
			recordInfoVO1016.rechargeMoney = 0;
			recordInfoVO1016.isSend = 0;
		} else {
			recordInfoVO1016.rechargeMoney = list.get(0);
			recordInfoVO1016.isSend = list.get(1);
		}
		return recordInfoVO1016;
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
