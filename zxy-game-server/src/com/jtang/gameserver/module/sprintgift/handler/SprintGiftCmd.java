package com.jtang.gameserver.module.sprintgift.handler;

/**
 * 等级礼包命令接口
 * @author ligang
 *
 */
public interface SprintGiftCmd {
	
	/**
	 * 获取等级礼包领取信息   1 已领取 （不发）  2  已达到未领取  3 未达到 
	 * <pre>
	 * 请求：{@code Request}
	 * 响应:{@code SprintGiftStatusListResponse}
	 * </pre>
	 */
	byte GET_SPRINT_GIFT_LIST = 1;
	
	/**
	 * 领取指定等级礼包
	 * <pre>
	 * 请求:{@code ReceiveSpecifySprintGiftRequest}
	 * 响应:{@code Response}
	 * </pre>
	 */
	byte GET_SPECIFY_SPRINT_GIFT = 2;
	
}
