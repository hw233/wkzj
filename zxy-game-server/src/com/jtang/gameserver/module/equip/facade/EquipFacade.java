package com.jtang.gameserver.module.equip.facade;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiatang.common.model.EquipVO;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.equip.type.EquipAttributeKey;
import com.jtang.gameserver.module.equip.type.EquipDecreaseType;

public interface EquipFacade {

	/**
	 * 获取某角色身上的装备
	 * @param actorId
	 * @param uuid
	 * @return
	 */
	EquipVO get(long actorId, long uuid);
	
	/**
	 * 获取当前角色下的所有装备
	 * @param actorId
	 * @return
	 */
	Collection<EquipVO> getList(long actorId);
	
	/**
	 * 给某个角色添加装备(自动推送)
	 * @param actorId	角色id
	 * @param addType		装备添加类型
	 * @param equipId	装备id
	 * @return	返回装备uuid, null为失败
	 */
	TResult<Long> addEquip(long actorId, EquipAddType addType, int equipId);
	
	/**
	 * 给某个角色添加装备(自动推送)
	 * @param actorId		角色Id
	 * @param addType		装备添加类型
	 * @param equipIds		装备列表
	 * @return 返回装备uuid
	 */
	TResult<Long[]> addEquip(long actorId, EquipAddType addType, Set<Integer> equipIds);
	
	/**
	 * 出售装备(自动推送)
	 * @param actorId		角色Id
	 * @param uuid			装备uuid
	 * @return
	 */
	short sellEquip(long actorId,EquipDecreaseType type, long uuid);
	
	/**
	 * 删除装备
	 * @param actorId		角色id
	 * @param uuidList		装备uuid列表
	 * @return
	 */
	short delEquip(long actorId,EquipDecreaseType type, List<Long> uuidList);
	
	/**
	 * 装备属性更新(自动推送属性)
	 * @param actorId			角色id
	 * @param uuid				装备uuid
	 * @param attributeMaps		装备变更的属性 {@code EquipAttributeKey}
	 * @return 返回状态码{@code StatusCode} {@code StatusCodeConstant}
	 */
	short updateAttribute(long actorId, long uuid, Map<EquipAttributeKey, Number> attributeMaps);
	
	/**
	 * 是否能合成
	 * @param actorId	角色 id
	 * @param 允许合成次数
	 * @return
	 */
	boolean canCompose(long actorId, int times);
	
	/**
	 * 记录已使用合成
	 * @param actorId	角色 id
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
	 * 检查能否删除
	 * @param actorId
	 * @param uuidList
	 * @return
	 */
	short canDelete(long actorId, List<Long> uuidList);

	/**
	 * 获取装备已重置次数
	 * @param actorId
	 * @return
	 */
	public int getResetNum(long actorId);

	/**
	 * 增加装备重置次数
	 * @param actorId
	 */
	public void addResetNum(long actorId);

	/**
	 * 重置装备已重置次数
	 * @param actorId
	 */
	void flushResetNum(Long actorId);

	/**
	 * 强化精炼装备到最高等级
	 * @param actorId
	 * @param equipList
	 * @return
	 */
	Result upEquip(long actorId, List<Long> equipList);
	
}
