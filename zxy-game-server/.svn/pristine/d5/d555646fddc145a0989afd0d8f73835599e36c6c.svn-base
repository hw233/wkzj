package com.jtang.gameserver.module.app.model.extension.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseGlobalInfoVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.app.type.AppKey;
/**
 * 赢在起跑线活动
 * 格式：actorId_actorId_actorId
 * @author ludd
 *
 */
@AppVO
public class GlobalInfoVO17 implements BaseGlobalInfoVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3923649587761673578L;
	/**
	 * 最强势力第一名
	 */
	public long maxPowerActorId;
	
	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_17;
	}

	@Override
	public void init(String record) {
		String[] items = StringUtils.split(record, Splitable.ATTRIBUTE_SPLIT);
		items = StringUtils.fillStringArray(items, 2, "0");
		if(record != null && "".equals(record) == false){
			maxPowerActorId = Long.valueOf(record);
		}
		
	}

	@Override
	public String parse2String() {
		List<Object> list = new ArrayList<>();
		list.add(maxPowerActorId);
		return StringUtils.collection2SplitString(list, Splitable.ATTRIBUTE_SPLIT);
	}

	@Override
	public Map<AppKey, Object> getGlobalInfoMaps() {
		Map<AppKey, Object> map = new HashMap<AppKey, Object>();
		map.put(AppKey.MAX_ACTOR_POWER, maxPowerActorId);
		return map;
	}

}
