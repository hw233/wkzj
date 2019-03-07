package com.jtang.gameserver.module.hero.facade;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Heros;
import com.jtang.gameserver.module.hero.model.HeroUnit;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.hero.type.HeroDecreaseType;



/**
 * 仙人对外接口
 * @author 0x737263
 *
 */
public interface HeroFacade {	
	/**
	 * 获取某个仙人的信息(根据仙人的配置ID取)
	 * @param actorId
	 * @param heroId
	 * @return
	 */
	HeroVO getHero(long actorId, int heroId);
	
	/**
	 * 增加一个仙人(仙人的属性根据配置文件初始化)
	 * 注意事项：
	 * 1.同名仙人只能有一个
	 * @param actorId 角色ID
	 * @param addType
	 * @param heroId 仙人的配置表id
	 */
	TResult<HeroVO> addHero(long actorId, HeroAddType addType, int heroId);
	
	/**
	 * 批量添加多个仙人
	 * @param actorId	角色id
	 * @param addType
	 * @param heroIds	多个仙人id集合
	 * @return
	 */
	Result addHero(long actorId, HeroAddType addType, Set<Integer> heroIds);

	/**
	 * 更新仙人信息
	 * @param actorId	角色ID
	 * @param hero		
	 * @return	true:更新成功  false：仙人信息不存在
	 */
	boolean updateHero(long actorId, HeroVO hero);

	/**
	 * 批量移除仙人
	 * @param actorId		角色ID
	 * @param decreaseType
	 * @param heroIds		仙人id列表
	 * @return	true:更新成功  false:仙人信息不存在
	 */
	short removeHero(long actorId, HeroDecreaseType decreaseType, List<Integer> heroIds);
	
	/**
	 * 判断该类型的仙人(同一个配置ID的仙人)是否存在
	 * @param actorId	角色id
	 * @param heroId	仙人id
	 * @return
	 */
	boolean isHeroExits(long actorId, int heroId);
	
	/**
	 * 根据星级获取仙人总数
	 * @param actorId	角色id
	 * @param star		仙人星级
	 * @return
	 */
	List<Integer> getStarHeroIds(long actorId, int star);
	
	/**
	 * 返回列表
	 * @param actorId
	 * @return
	 */
	Collection<HeroVO> getList(long actorId);
	
	/**
	 * 魂魄转仙人
	 * @param actorId	角色id
	 * @param heroId	仙人id
	 * @return
	 */
	TResult<HeroUnit> soul2Hero(long actorId, int heroId);
		
	/**
	 * 仙人突破
	 * @param actorId	角色id
	 * @param heroId	仙人id
	 * @return
	 */
	Result breakThrough(long actorId, int heroId);
	
	/**
	 * 提升仙人经验
	 * @param actorId	角色id
	 * @param heroId	仙人id
	 * @param exp		新增加的经验
	 */
	Result addHeroExp(long actorId, int heroId, int exp);
	
	/**
	 * 返回英雄数量
	 * @param actorId
	 * @return
	 */
	int size(long actorId);
	
	/**
	 * 获取仙人的基础数值(包括固定值加成,百分比加成的不算)
	 * @param actorId
	 * @param heroId
	 * @return
	 */
	public int getHeroBaseAttribute(long actorId, int heroId, HeroVOAttributeKey key);
	
	/**
	 * 获取仙人实体
	 * @param actorId
	 * @return
	 */
	Heros getHeros(long actorId);
	
	/**
	 * 设置合成数据
	 * @param actorId
	 * @param time 时间
	 */
	void recordCompose(long actorId);
	
	/**
	 * 检查是否可以合成
	 * @param actorId
	 * @param num 最大合成次数
	 * @return
	 */
	boolean canCompose(long actorId, int num);
	/**
	 * 检测并重置合成数据
	 * @param actorId 角色id
	 */
	void checkAndReset(long actorId);
	
	
	/**
	 * 检查列表英雄是否能被消耗
	 * @param actorId
	 * @param heroIds
	 * @return
	 */
	public short canDelete(long actorId, List<Integer> heroIds);
	
	/**
	 * 获取仙人合成次数
	 * @param actorId
	 * @return
	 */
	public int getComposeNum(long actorId);

	/**
	 * 获取仙人已重置次数
	 * @param actorId
	 * @return
	 */
	public int getResetNum(long actorId);

	/**
	 * 增加仙人重置次数
	 * @param actorId
	 */
	public void addResetNum(long actorId);

	/**
	 * 重置仙人已重置次数
	 * @param actorId
	 */
	public void flushResetNum(Long actorId);
	
	/**
	 * 获取已经拥有过的仙人
	 */
	public List<Integer> getHeroIds(long actorId);
}
