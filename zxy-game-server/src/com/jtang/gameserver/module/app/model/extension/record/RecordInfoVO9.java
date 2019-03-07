package com.jtang.gameserver.module.app.model.extension.record;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.app.type.AppKey;

/**
 * 冲级送话费
 * @author jianglf
 *
 */
@AppVO
public class RecordInfoVO9 implements BaseRecordInfoVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3172053182355103711L;
	/**
	 * 是否完成
	 * 0.不可领取   1.可领取  2.已领取
	 */
	public int isFinish;
	
	@Override
	public Map<AppKey, Object> getRecordInfoMaps() {
		Map<AppKey , Object> map = new HashMap<AppKey, Object>();
		map.put(AppKey.IS_FINISH, this.isFinish);
		return map;
		
	}

	@Override
	public String parse2String() {
		return String.valueOf(isFinish);
	}


	@Override
	public BaseRecordInfoVO valueOf(String record) {
		RecordInfoVO9 vo = new RecordInfoVO9();
		if(StringUtils.isNotBlank(record)){
			vo.isFinish = Integer.valueOf(record);
		}
		return vo;
	}

	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_9;
	}
}
