package com.jtang.gameserver.module.app.model.extension.record;

import java.util.HashMap;
import java.util.Map;

import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.app.type.AppKey;

/**
 * 每天登陆领取宝箱
 * @author jianglf
 *
 */
@AppVO
public class RecordInfoVO3 implements BaseRecordInfoVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4912029037067868499L;
	/**
	 * 是否已领取 0.否 1.是
	 */
	public int isGet;

	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_3;
	}

	@Override
	public String parse2String() {
		return String.valueOf(isGet);
	}

	@Override
	public BaseRecordInfoVO valueOf(String record) {
		RecordInfoVO3 recordInfoVO1003 = new RecordInfoVO3();
		if(record.isEmpty()){
			recordInfoVO1003.isGet = 0;
		}else{
			recordInfoVO1003.isGet = Integer.valueOf(record);
		}
		return recordInfoVO1003;
	}

	@Override
	public Map<AppKey, Object> getRecordInfoMaps() {
		Map<AppKey , Object> map = new HashMap<AppKey, Object>();
		map.put(AppKey.IS_GET, isGet);
		return map;
	}

	public boolean isGet() {
		if(isGet == 1){
			return true;
		}
		return false;
	}

}
