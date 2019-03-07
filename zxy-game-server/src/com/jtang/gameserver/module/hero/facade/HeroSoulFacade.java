package com.jtang.gameserver.module.hero.facade;

import java.util.List;

import com.jtang.core.result.Result;
import com.jtang.gameserver.dbproxy.entity.HeroSoul;
import com.jtang.gameserver.module.hero.model.HeroSoulVO;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulDecreaseType;

/**
 * 仙人魂魄对外接口
 * @author 0x737263
 *
 */
public interface HeroSoulFacade {
	
	/**
	 * 获取仙人魂魄列表
	 * @param actorId
	 * @return
	 */
	HeroSoul getSoul(long actorId);
	
	/**
	 * 添加魂魄(自动推送)
	 * @param actorId	角色id
	 * @param type		魂魄添加类型
	 * @param heroId	仙人魂魄id
	 * @param count		数量
	 * @return
	 */
	Result addSoul(long actorId, HeroSoulAddType type, int heroId, int count);
	
	/**
	 * 扣除一定数量的魂魄(自动推送)
	 * @param actorId	角色id
	 * @param type		魂魄扣除类型
	 * @param heroId	仙人魂魄id
	 *  @param count	数量
	 */
	Result reductSoul(long actorId, HeroSoulDecreaseType type, int heroId, int count);
	
	/**
	 * 获取列表
	 * @param actorId
	 * @return
	 */
	List<HeroSoulVO> getList(long actorId);
	
	/**
	 * 获取某个仙人魂魄的数量
	 * @param actorId
	 * @param heroId
	 * @return
	 */
	int getSoulNum(long actorId, int heroId);
	
}
