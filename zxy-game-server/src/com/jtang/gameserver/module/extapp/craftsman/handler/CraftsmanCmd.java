package com.jtang.gameserver.module.extapp.craftsman.handler;

public interface CraftsmanCmd {

	/**
	 * 打造
	 * 请求:{@code BuildEquipRequest}
	 * 响应:{@code BuildEquipResponse}
	 */
	byte BUILD = 1;
	
	/**
	 * 活动状态
	 * 请求:{@code Request}
	 * 响应:{@code CraftsmanStatusResponse}
	 * 推送:{@code CraftsmanStatusResponse}
	 */
	byte STATUS = 2;
}
