package com.jtang.gameserver.module.equip.handler;

public interface EquipCmd {

	/**
	 * 请求装备列表
	 * <pre>
	 * 请求：{@code Request}
	 * 响应：{@code EquipListResponse}
	 * </pre>
	 */
	byte GET_EQUIP_LIST = 1;
	
	/**
	 * 请求出售装备
	 * <pre>
	 * 请求：{@code EquipSellRequest}
	 * 响应：{@code Response}
	 * </pre>
	 */
	byte SELL_EQUIP = 2;
	
	/**
	 * 推送添加装备消息
	 * <pre>
	 * 推送：{@code EquipListResponse}
	 * </pre>
	 */
	byte PUSH_ADD_EQUIP = 3;
	
	/**
	 * 推送删除装备消息
	 * <pre>
	 * 推送：{@code EquipDelResponse}
	 * </pre>
	 */
	byte PUSH_DEL_EQUIP = 4;

	/**
	 * 推送装备属性更新
	 * <pre>
	 * 推送：{@code EquipAttributeResponse}
	 * </pre>
	 */
	byte PUSH_EQUIP_ATTRUBITE = 5;
	
	/**
	 * 强化精炼装备到最高等级
	 * 请求:{@code EquipUpRequest}
	 * 返回:{@code Response}
	 */
	byte UP_EQUIP = 6;
	
	
}
