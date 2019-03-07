package com.jtang.gameserver.module.app.model.extension.record;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;

/**
 * 赢在起跑线
 * rewardTime_isGet
 * @author jianglf
 *
 */
@AppVO
public class RecordInfoVO18 implements BaseRecordInfoVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5366889446034001285L;
	
	/**
	 * 领取状态
	 * key:天数
	 * value:0.不可领取  1.已领取
	 */
	public Map<Integer,Integer> rewardType = new HashMap<>();
	
	/**
	 * 累计登陆天数
	 */
	public int loginDay;
	
	/**
	 * 上次登陆时间
	 */
	public int loginTime;

	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_18;
	}

	@Override
	public String parse2String() {
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtils.map2DelimiterString(rewardType,Splitable.DELIMITER_ARGS,Splitable.BETWEEN_ITEMS));// key:value,key:value
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(loginDay);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(loginTime);
		return sb.toString();
	}

	@Override
	public BaseRecordInfoVO valueOf(String record) {
		RecordInfoVO18 recordInfoVO18 = new RecordInfoVO18();
		String[] str = StringUtils.split(record, Splitable.ATTRIBUTE_SPLIT);
		if(str != null){
			str = StringUtils.fillStringArray(str, 3, "");
			String rewardString = str[0];
			recordInfoVO18.loginDay = Integer.valueOf(str[1]);
			recordInfoVO18.loginTime = Integer.valueOf(str[2]);
			recordInfoVO18.rewardType = StringUtils.keyValueString2IntMap(rewardString);
		}
		return recordInfoVO18;
	}

	@Override
	public Map<AppKey, Object> getRecordInfoMaps() {
		Map<AppKey,Object> map = new HashMap<AppKey, Object>();
		map.put(AppKey.LOGIN_DAY, loginDay);
		return map;
	}

	/**
	 * 是否已领取
	 */
	public boolean isGet(int loginDay){
		int state = rewardType.get(loginDay);
		return state == 1 ? true : false;
	}
}
