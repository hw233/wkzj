package com.jtang.gameserver.module.app.model.extension.global;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseGlobalInfoVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.app.type.AppKey;

@AppVO
public class GlobalInfoVO7 implements BaseGlobalInfoVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6102877019370910769L;
	/**
	 * 充值前三名
	 */
	public List<Long> actors = new ArrayList<Long>();
	
	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_7;
	}

	@Override
	public void init(String record) {
		String[] items = StringUtils.split(record, Splitable.ELEMENT_SPLIT);
		if(items != null){
			items = StringUtils.fillStringArray(items, 3, "0");
			for(String str:items){
				actors.add(Long.valueOf(str));
			}
		}
	}

	@Override
	public String parse2String() {
		return StringUtils.collection2SplitString(actors, Splitable.ELEMENT_DELIMITER);
	}

	@Override
	public Map<AppKey, Object> getGlobalInfoMaps() {
		Map<AppKey, Object> map = new ConcurrentHashMap<AppKey, Object>();
		if(actors.size() == 1){
			map.put(AppKey.ONE_PAY_ACTOR, actors.get(0));
		}else if(actors.size() == 2){
			map.put(AppKey.ONE_PAY_ACTOR, actors.get(0));
			map.put(AppKey.TWO_PAY_ACTOR, actors.get(1));
		}else if(actors.size() == 3){
			map.put(AppKey.ONE_PAY_ACTOR, actors.get(0));
			map.put(AppKey.TWO_PAY_ACTOR, actors.get(1));
			map.put(AppKey.THREE_PAY_ACTOR, actors.get(2));
		}
		return map;
	}

}
