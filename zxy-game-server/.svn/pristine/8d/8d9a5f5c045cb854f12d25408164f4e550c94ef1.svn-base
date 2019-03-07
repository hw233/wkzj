package com.jtang.gameserver.module.app.model.extension.record;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.app.type.AppKey;

/**
 * 一元礼包
 * 格式:物品id_购买次数|物品id_购买次数
 * @author jianglf
 *
 */
@AppVO
public class RecordInfoVO5 implements BaseRecordInfoVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5416467126646397321L;
	public Map<Integer, Integer> recordVOs = new HashMap<>();

	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_5;
	}

	@Override
	public String parse2String() {
		return StringUtils.numberMap2String(recordVOs);
	}

	@Override
	public BaseRecordInfoVO valueOf(String record) {
		RecordInfoVO5 recordInfoVO1005 = new RecordInfoVO5();
		recordInfoVO1005.recordVOs = StringUtils.delimiterString2IntMap(record);
		return recordInfoVO1005;
	}

	@Override
	public Map<AppKey, Object> getRecordInfoMaps() {
		Map<AppKey, Object> map = new HashMap<AppKey, Object>();
		map.put(AppKey.APP_STRING, parse2String());
		return map;
	}

}