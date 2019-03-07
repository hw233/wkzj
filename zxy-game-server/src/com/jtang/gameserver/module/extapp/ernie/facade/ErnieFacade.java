package com.jtang.gameserver.module.extapp.ernie.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieInfoResponse;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieRecordResponse;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieStatusResponse;


public interface ErnieFacade {

	/*
	 * 获取活动状态
	 */
	TResult<ErnieStatusResponse> getStatus();
	
	/**
	 * 获取摇奖奖励信息
	 * @param actorId
	 * @return
	 */
	TResult<ErnieRecordResponse> getRecord(long actorId);
	
	/**
	 * 获取摇奖活动信息
	 * @param actorId
	 * @return
	 */
	TResult<ErnieInfoResponse> getInfo(long actorId);
	
	/**
	 * 摇奖
	 * @param actorId
	 * @return
	 */
	TResult<ErnieInfoResponse> runErnie(long actorId);
	
	
	
	
}
