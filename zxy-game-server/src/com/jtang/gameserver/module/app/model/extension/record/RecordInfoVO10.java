package com.jtang.gameserver.module.app.model.extension.record;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.app.type.AppKey;

/**
 * 登陆领xx点券
 * @author jianglf
 *
 */
@AppVO
public class RecordInfoVO10 implements BaseRecordInfoVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6285196175019923202L;
	/**
	 * 是否领取 0.未领取 1.已领取
	 */
	public int isGet;

	@Override
	public Map<AppKey, Object> getRecordInfoMaps() {
		Map<AppKey, Object> map = new HashMap<AppKey, Object>();
		map.put(AppKey.IS_GET, this.isGet);
		return map;
	}

	@Override
	public String parse2String() {
		return String.valueOf(isGet);
	}

	@Override
	public BaseRecordInfoVO valueOf(String record) {
		RecordInfoVO10 vo = new RecordInfoVO10();
		if (StringUtils.isNotBlank(record)) {
			vo.isGet = Integer.valueOf(record);
		}
		return vo;
	}

	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_10;
	}

	public boolean isGet() {
		return isGet == 1;
	}

}
