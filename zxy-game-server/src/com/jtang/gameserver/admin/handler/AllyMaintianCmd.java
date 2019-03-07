package com.jtang.gameserver.admin.handler;



public interface AllyMaintianCmd {
	
	/**
	 * 添加盟友
	 * 请求:{@code AddAllayRequest}
	 * 响应:{@code Response}
	 */
	byte ADD_ALLY = 1;
	
	/**
	 * 删除盟友
	 * 请求:{@code DeleteAllayRequest}
	 * 响应:{@code Response}
	 */
	byte DEL_ALLY = 2;
}
