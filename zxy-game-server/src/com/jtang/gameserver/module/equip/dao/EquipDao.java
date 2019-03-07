package com.jtang.gameserver.module.equip.dao;

import java.util.Collection;

import com.jiatang.common.model.EquipVO;
import com.jtang.gameserver.dbproxy.entity.Equips;

/**
 * 装备库dao
 * @author liujian
 *
 */
public interface EquipDao {

	/**
	 * 获取当前角色下的所有装备
	 * @param actorId
	 * @return
	 */
	EquipVO get(long actorId, long uuid);
	/**
	 * 获取装备实体
	 * @param actorId
	 * @return
	 */
	Equips get(long actorId);
	
	/**
	 * 获取装备列表
	 * @param actorId
	 * @return
	 */
	Collection<EquipVO> getList(long actorId);
	
	/**
	 * 获取装备总数
	 * @param actorId
	 * @return
	 */
	int getCount(long actorId);
	
	/**
	 * 添加装备
	 * @param actorId
	 * @param equipVo
	 * @return
	 */
	boolean add(long actorId, EquipVO equipVo);
	
	/**
	 * 移除某一装备
	 * @param actorId
	 * @param uuid
	 * @return
	 */
	boolean remove(long actorId, long uuid);
	
	/**
	 * 更新某一装备
	 * @param actorId
	 * @param equipVo
	 * @return
	 */
	boolean update(long actorId, EquipVO equipVo);
	
	/**
	 * 是否能合成
	 * @param actorId	角色 id
	 * @param 允许合成次数
	 * @return
	 */
	boolean canCompose(long actorId, int num);
	
	/**
	 * 记录已使用合成
	 * @param actorId
	 */
	void recordCompose(long actorId);

	/**
	 * 检测并重置合成时间和次数
	 * @param actorId
	 */
	void chechAndResetCompose(long actorId);
	/**
	 * 获取合成次数
	 * @param actorId
	 * @return
	 */
	int getComposeNum(long actorId);
	/**
	 * 记录重置次数
	 * @param actorId
	 */
	void upResetNum(long actorId);
	/**
	 * 更新
	 */
	void update(Equips equips);
	
}
