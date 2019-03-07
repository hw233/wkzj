package com.jtang.gameserver.module.adventures.favor.effect;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.module.adventures.favor.type.FavorTriggerType;
/**
 * 触发条件解析器
 * @author ludd
 *
 */
@Component
public abstract class FavorTrigger {

	private static Map<Integer, FavorTrigger> triggers = new HashMap<Integer, FavorTrigger>();
	
	@PostConstruct
	private void init(){
		triggers.put(this.getTriggerType(), this);
	}
	/**
	 * 获取触发条件id
	 * @return
	 */
	public abstract int getTriggerType();
	
	/**
	 * 是否触发
	 * @param actorId
	 * @return
	 */
	public abstract boolean isTrigger(long actorId);
	/**
	 * 获取触发器
	 * @param type1
	 */
	public static FavorTrigger getTrigger(FavorTriggerType type1) {
		return triggers.get(type1.getType());
	}
}
