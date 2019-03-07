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
 * 赢在起跑线
 * rewardTime_isGet
 * @author jianglf
 *
 */
@AppVO
public class RecordInfoVO6 implements BaseRecordInfoVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1729086832162492746L;
	/**
	 * 领取时间
	 */
	public int rewardTime;
	/**
	 * 0:未领取 1：领取
	 */
	public int isGet;
	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_6;
	}

	@Override
	public String parse2String() {
		List<Object> list = new ArrayList<>();
		list.add(this.rewardTime);
		list.add(this.isGet);
		return StringUtils.collection2SplitString(list, Splitable.ATTRIBUTE_SPLIT);
	}

	@Override
	public BaseRecordInfoVO valueOf(String record) {
		String[] items = StringUtils.split(record, Splitable.ATTRIBUTE_SPLIT);
		RecordInfoVO6 recordInfoVO1006 = new RecordInfoVO6();
		if(items != null){
			items = StringUtils.fillStringArray(items, 2, "0");
			recordInfoVO1006.rewardTime = Integer.valueOf(items[0]);
			recordInfoVO1006.isGet = Integer.valueOf(items[1]);
		}
		return recordInfoVO1006;
	}

	@Override
	public Map<AppKey, Object> getRecordInfoMaps() {
		Map<AppKey, Object> map = new HashMap<AppKey, Object>();
		map.put(AppKey.IS_GET, this.isGet);
		return map;
	}

	/**
	 * 是否已领取
	 * @return
	 */
	public boolean isGet() {
		return isGet == 1;
	}

}
