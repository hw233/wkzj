package com.jtang.gameserver.module.app.model.extension.record;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.app.type.AppKey;

/**
 * 七天大礼包
 * 3天:领取状态,5天:领取状态,7天:领取状态_连续登陆天数_上次登陆时间
 * 领取状态:(0.不可领取 1.可领取 2.已领取)
 * @author jianglf
 *
 */
@AppVO
public class RecordInfoVO8 implements BaseRecordInfoVO { 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4690270539117802910L;

	/**
	 * 奖励领取情况
	 * key : 领奖的天数
	 * value : 是否已领取 0.不可领取 1.可领取 2.已领取;
	 */
	public Map<Integer,Integer> reward = new HashMap<Integer, Integer>();
	
	/**
	 * 连续登陆天数
	 */
	public int loginDay;
	
	/**
	 * 上次登陆时间
	 */
	public int loginTime;
	
	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_8;
	}

	@Override
	public String parse2String() {
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtils.map2DelimiterString(reward,Splitable.DELIMITER_ARGS,Splitable.BETWEEN_ITEMS));// key:value,key:value
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(loginDay);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(loginTime);
		return sb.toString();
	}

	@Override
	public BaseRecordInfoVO valueOf(String record) {
		RecordInfoVO8 recordInfoVO1008 = new RecordInfoVO8();
		String[] str = StringUtils.split(record, Splitable.ATTRIBUTE_SPLIT);
		if(str != null){
			str = StringUtils.fillStringArray(str, 3, "");
			String rewardString = str[0];
			recordInfoVO1008.loginDay = Integer.valueOf(str[1]);
			recordInfoVO1008.loginTime = Integer.valueOf(str[2]);
			recordInfoVO1008.reward = StringUtils.keyValueString2IntMap(rewardString);
		}else{
			Map<Integer,Integer> map = new HashMap<Integer,Integer>();
			map.put(5, 0);
			map.put(7, 0);
			recordInfoVO1008.reward = map;
		}
		return recordInfoVO1008;
	}

	@Override
	public Map<AppKey, Object> getRecordInfoMaps() {
		String mapString = StringUtils.map2DelimiterString(reward,Splitable.DELIMITER_ARGS,Splitable.BETWEEN_ITEMS);
		Map<AppKey,Object> map = new HashMap<AppKey, Object>();
		map.put(AppKey.APP_STRING, mapString);
		map.put(AppKey.LOGIN_DAY, loginDay);
		return map;
	}

}
