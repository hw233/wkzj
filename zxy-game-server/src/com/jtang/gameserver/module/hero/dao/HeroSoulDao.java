package com.jtang.gameserver.module.hero.dao;

import java.util.List;

import com.jtang.gameserver.dbproxy.entity.HeroSoul;
import com.jtang.gameserver.module.hero.model.HeroSoulVO;

/**
 * 仙人魂魄数据访问对象
 * @author 0x737263
 *
 */
public interface HeroSoulDao {

	/**
	 * 获取仙人魂魄
	 * @param actorId
	 * @return
	 */
	HeroSoul get(long actorId);

	/**
	 * 更新仙人魂魄
	 * @param heroSoul
	 * @return
	 */
	boolean update(HeroSoul heroSoul);
	
	/**
	 * 获取列表
	 * @param actorId
	 * @return
	 */
	List<HeroSoulVO> getList(long actorId);
}
