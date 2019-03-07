package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.module.hero.type.HeroAddType;


/**
 * 新增英雄事件
 * @author vinceruan
 *
 */
public class AddHeroEvent extends GameEvent {
	
	/**
	 * 英雄id
	 */
	public int heroId;
	
	/**
	 * 添加的仙人类型
	 */
	public HeroAddType addType;
	
	/**
	 * 仙人配置
	 */
	public HeroConfig config;
	
	public AddHeroEvent(long actorId, int heroId, HeroAddType addType, HeroConfig config) {
		super(EventKey.ADD_HERO, actorId);
		this.heroId = heroId;
		this.addType = addType;
		this.config = config;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(heroId);
		list.add(addType);
		return list;
	}
}
