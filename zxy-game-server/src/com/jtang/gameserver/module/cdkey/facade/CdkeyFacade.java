package com.jtang.gameserver.module.cdkey.facade;

import com.jtang.core.lop.result.ListLopResult;
import com.jtang.gameserver.component.lop.response.CdkeyLOPResponse;

public interface CdkeyFacade {

	/**
	 * 获取礼包通过激活码
	 * @param cdkey		激活码
	 * @param actorId	角色id
	 * @return
	 */
	ListLopResult<CdkeyLOPResponse> getPackage(String cdkey, long actorId);
	
}
