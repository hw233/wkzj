package com.jtang.gameserver.module.app.model.extension.record;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;

@AppVO
public class RecordInfoVO15 implements BaseRecordInfoVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4158387729477383405L;

	/**
	 * 仙人id_购买次数|...
	 */
	public Map<Integer,Integer> map = new HashMap<>();
	
	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_15;
	}

	@Override
	public String parse2String() {
		return StringUtils.map2DelimiterString(map,Splitable.ATTRIBUTE_SPLIT,Splitable.ELEMENT_DELIMITER);
	}

	@Override
	public BaseRecordInfoVO valueOf(String record) {
		RecordInfoVO15 recordInfo1015 = new RecordInfoVO15();
		recordInfo1015.map = StringUtils.delimiterString2IntMap(record);
		return recordInfo1015;
	}

	@Override
	public Map<AppKey, Object> getRecordInfoMaps() {
		Map<AppKey,Object> appMap = new HashMap<>();
		appMap.put(AppKey.APP_STRING, parse2String());
		return appMap;
	}

}
