package com.jtang.core.event;

import java.util.List;

import com.jtang.core.utility.StringUtils;

/**
 * 游戏相关事件
 * @author 0x737263
 *
 */
public abstract class GameEvent extends Event {

	/**
	 * 角色id
	 */
	public long actorId;
	
	public GameEvent(String name, long actorId) {
		super(name);
		this.actorId = actorId;
	}
	
	/**
	 * 获取参数集合
	 * @return
	 */
	public abstract List<Object> getParamsList();
	
	/**
	 * 输出当前事件的参数
	 * @return
	 */
	public String params2String() {
		return StringUtils.objectArray2DelimiterString(getParamsList());
	}
	
}
