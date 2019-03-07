package com.jtang.gameserver.module.equipdevelop.handler;

/**
 * 装备突破模块指令
 * @author hezh
 *
 */
public interface EquipDevelopCmd {

	/**
	 * 装备、装备碎片提炼
	 * <pre>
	 * 请求：{@code EquipConvertRequest}
	 * 响应：{@code Response}
	 * </pre>
	 */
	byte EQUIP_CONVERT = 1;
	
	/**
	 * 装备突破
	 * <pre>
	 * 请求：{@code EquipDevelopRequest}
	 * 响应：{@code EquipDevelopResponse}
	 * </pre>
	 */
	byte EQUIP_DEVELOP = 2;
}
