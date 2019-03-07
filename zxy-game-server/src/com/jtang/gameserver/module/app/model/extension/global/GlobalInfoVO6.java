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
public class GlobalInfoVO6 implements BaseGlobalInfoVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3923649587761673578L;
	/**
	 * 最高等级角色id
	 */
	public long maxLevelActorId;
	
	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_6;
	}

	@Override
	public void init(String record) {
		if(record != null && "".equals(record) == false){
			maxLevelActorId = Long.valueOf(record);
		}
	}

	@Override
	public String parse2String() {
		List<Object> list = new ArrayList<>();
		list.add(maxLevelActorId);
		return StringUtils.collection2SplitString(list, Splitable.ATTRIBUTE_SPLIT);
	}

	@Override
	public Map<AppKey, Object> getGlobalInfoMaps() {
		Map<AppKey, Object> map = new HashMap<AppKey, Object>();
		map.put(AppKey.MAX_ACTOR_LEVEL, maxLevelActorId);
		return map;
	}

}
